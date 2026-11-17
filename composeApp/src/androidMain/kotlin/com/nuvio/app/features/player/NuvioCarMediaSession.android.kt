package com.nuvio.app.features.player

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaDescription
import android.media.MediaMetadata
import android.media.browse.MediaBrowser
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import java.io.File
import java.io.FileOutputStream

// AAOS fork: one process-wide MediaSession shared by the in-app player and
// NuvioCarMediaBrowserService. AAOS only treats apps with a MediaBrowserService as media
// sources, and its media card reads the session token handed out by that service, so the
// player must publish to this same session. The last item is saved to disk so the card can
// be refilled, without network, after the app process has been closed.
internal object NuvioCarMediaSession {
    private const val TAG = "NuvioCarMediaSession"
    private const val PREFS_NAME = "nuvio_car_media_card"
    private const val KEY_TITLE = "title"
    private const val KEY_SUBTITLE = "subtitle"
    private const val KEY_ARTWORK_URL = "artwork_url"
    private const val KEY_POSITION_MS = "position_ms"
    private const val KEY_DURATION_MS = "duration_ms"
    private const val ARTWORK_FILE_NAME = "car_media_card_artwork.png"
    private const val POSITION_SAVE_INTERVAL_MS = 5_000L

    // Car.CAR_EXTRA_BROWSE_SERVICE_FOR_SESSION: tells CarMediaService which browse service owns this session.
    private const val CAR_EXTRA_BROWSE_SERVICE_FOR_SESSION = "android.media.session.BROWSE_SERVICE"

    // Cars with this feature run a car media app that opens the activity named in the service's
    // androidx.car.app.media.CalMediaActivityComponent meta-data instead of its own media screen.
    internal const val FEATURE_CAR_TEMPLATES_HOST_MEDIA = "android.software.car.templates_host.media"

    // androidx.media.utils.MediaConstants: AAOS shows a button that launches this PendingIntent.
    private const val EXTRA_ERROR_RESOLUTION_ACTION_LABEL = "android.media.extras.ERROR_RESOLUTION_ACTION_LABEL"
    private const val EXTRA_ERROR_RESOLUTION_ACTION_INTENT = "android.media.extras.ERROR_RESOLUTION_ACTION_INTENT"
    private const val OPEN_APP_PROMPT_MS = 15_000L
    private val PROMPT_TOKEN = Any()

    const val LAST_PLAYED_MEDIA_ID = "nuvio_last_played"
    const val CONTINUE_WATCHING_ID = "nuvio_continue_watching"

    internal data class SavedItem(
        val title: String,
        val subtitle: String?,
        val artworkUrl: String?,
        val positionMs: Long,
        val durationMs: Long,
    )

    private val mainHandler = Handler(Looper.getMainLooper())
    private var session: MediaSession? = null
    private var playerOwner: Any? = null
    private var lastSavedPositionMs = Long.MIN_VALUE

    /** Must be called on the main thread. */
    fun get(context: Context): MediaSession {
        session?.let { return it }
        val appContext = context.applicationContext
        return MediaSession(appContext, NOW_PLAYING_TAG).apply {
            setFlags(
                MediaSession.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS,
            )
            setExtras(browseServiceExtras(appContext))
            buildLaunchIntent(appContext)?.let { intent ->
                setSessionActivity(
                    android.app.PendingIntent.getActivity(
                        appContext,
                        0,
                        intent,
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE,
                    ),
                )
            }
            setCallback(idleCallback(appContext), mainHandler)
            session = this
        }
    }

    /**
     * Switches on exactly one of the two browse services: the opted-in (launchable) one on cars
     * whose media app redirects to NuvioCarMediaEntryActivity, otherwise the plain one, so the app
     * icon always opens Nuvio. Only writes when the state differs. Called when Nuvio opens
     * (initializePlatform), never from a service the car is binding, which it could switch off.
     */
    fun selectBrowseService(context: Context) {
        val packageManager = context.packageManager
        val optIn = packageManager.hasSystemFeature(FEATURE_CAR_TEMPLATES_HOST_MEDIA)
        setComponentState(
            context,
            NuvioCarMediaLaunchableService::class.java,
            if (optIn) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
        )
        setComponentState(
            context,
            NuvioCarMediaBrowserService::class.java,
            if (optIn) PackageManager.COMPONENT_ENABLED_STATE_DISABLED else PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
        )
        session?.setExtras(browseServiceExtras(context))
    }

    /** The browse service that is switched on now (the opted-in one only after selectBrowseService). */
    internal fun activeBrowseServiceClass(context: Context): Class<out NuvioCarMediaBrowserService> {
        val launchable = ComponentName(context.packageName, NuvioCarMediaLaunchableService::class.java.name)
        val state = runCatching { context.packageManager.getComponentEnabledSetting(launchable) }
            .getOrDefault(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
        return if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            NuvioCarMediaLaunchableService::class.java
        } else {
            NuvioCarMediaBrowserService::class.java
        }
    }

    private fun browseServiceExtras(context: Context) = Bundle().apply {
        putString(CAR_EXTRA_BROWSE_SERVICE_FOR_SESSION, activeBrowseServiceClass(context).name)
    }

    private fun setComponentState(context: Context, service: Class<*>, state: Int) {
        val component = ComponentName(context.packageName, service.name)
        val packageManager = context.packageManager
        runCatching {
            if (packageManager.getComponentEnabledSetting(component) != state) {
                packageManager.setComponentEnabledSetting(component, state, PackageManager.DONT_KILL_APP)
            }
        }.onFailure { error -> Log.w(TAG, "Failed to set $component to $state", error) }
    }

    /** The in-app player takes over the session's transport controls. Main thread only. */
    fun attachPlayer(context: Context, owner: Any, callback: MediaSession.Callback) {
        playerOwner = owner
        get(context).setCallback(callback, mainHandler)
    }

    /** The player is gone; keep the metadata and let play requests reopen the app. Main thread only. */
    fun detachPlayer(context: Context, owner: Any) {
        if (playerOwner !== owner) return
        playerOwner = null
        get(context).setCallback(idleCallback(context.applicationContext), mainHandler)
    }

    /**
     * Called when the car binds the browse service with no player in this process: refills the
     * session from disk so the media card shows the last item, paused.
     */
    fun restoreIfIdle(context: Context) {
        if (playerOwner != null) return
        val mediaSession = get(context)
        if (mediaSession.controller.metadata != null) return
        val saved = loadSaved(context) ?: return
        val builder = MediaMetadata.Builder()
            .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, LAST_PLAYED_MEDIA_ID)
            .putString(MediaMetadata.METADATA_KEY_TITLE, saved.title)
            .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, saved.title)
        saved.subtitle?.let { subtitle ->
            builder.putString(MediaMetadata.METADATA_KEY_ARTIST, subtitle)
            builder.putString(MediaMetadata.METADATA_KEY_ALBUM, subtitle)
            builder.putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, subtitle)
            builder.putString(MediaMetadata.METADATA_KEY_DISPLAY_DESCRIPTION, subtitle)
        }
        artworkContentUri(context)?.toString()?.let { uri ->
            builder.putString(MediaMetadata.METADATA_KEY_ART_URI, uri)
            builder.putString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI, uri)
            builder.putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, uri)
        }
        if (saved.durationMs > 0L) builder.putLong(MediaMetadata.METADATA_KEY_DURATION, saved.durationMs)
        runCatching { mediaSession.setMetadata(builder.build()) }
            .onFailure { error -> Log.w(TAG, "Failed to restore metadata", error) }

        publishIdlePausedState(mediaSession, saved.positionMs)
        mediaSession.isActive = true
    }

    /** Root folder so the car's media screen shows a "Continue watching" tab instead of an empty list. */
    fun continueWatchingFolder(): MediaBrowser.MediaItem =
        MediaBrowser.MediaItem(
            MediaDescription.Builder()
                .setMediaId(CONTINUE_WATCHING_ID)
                .setTitle("Continue watching")
                .build(),
            MediaBrowser.MediaItem.FLAG_BROWSABLE,
        )

    /** Browse item for the car's media browser and the system's "recent" query. */
    fun lastPlayedBrowseItem(context: Context): MediaBrowser.MediaItem? {
        val saved = loadSaved(context) ?: return null
        val description = MediaDescription.Builder()
            .setMediaId(LAST_PLAYED_MEDIA_ID)
            .setTitle(saved.title)
            .setSubtitle(saved.subtitle)
            .setIconUri(artworkContentUri(context))
            .build()
        return MediaBrowser.MediaItem(description, MediaBrowser.MediaItem.FLAG_PLAYABLE)
    }

    fun saveItem(context: Context, title: String, subtitle: String?, artworkUrl: String?) {
        val prefs = prefs(context)
        val sameItem = prefs.getString(KEY_TITLE, null) == title &&
            prefs.getString(KEY_SUBTITLE, null) == subtitle
        val editor = prefs.edit()
            .putString(KEY_TITLE, title)
            .putString(KEY_SUBTITLE, subtitle)
            .putString(KEY_ARTWORK_URL, artworkUrl)
        if (!sameItem) {
            editor.putLong(KEY_POSITION_MS, 0L).putLong(KEY_DURATION_MS, 0L)
            lastSavedPositionMs = Long.MIN_VALUE
        }
        if (prefs.getString(KEY_ARTWORK_URL, null) != artworkUrl) {
            runCatching { artworkFile(context).delete() }
        }
        editor.apply()
    }

    fun savePlayback(context: Context, positionMs: Long, durationMs: Long, force: Boolean) {
        if (!force && lastSavedPositionMs != Long.MIN_VALUE &&
            kotlin.math.abs(positionMs - lastSavedPositionMs) < POSITION_SAVE_INTERVAL_MS
        ) {
            return
        }
        lastSavedPositionMs = positionMs
        prefs(context).edit()
            .putLong(KEY_POSITION_MS, positionMs.coerceAtLeast(0L))
            .putLong(KEY_DURATION_MS, durationMs.coerceAtLeast(0L))
            .apply()
    }

    /** Call off the main thread. */
    fun saveArtwork(context: Context, bitmap: Bitmap) {
        runCatching {
            FileOutputStream(artworkFile(context)).use { output ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            }
        }.onFailure { error -> Log.w(TAG, "Failed to save artwork", error) }
    }

    private fun loadSaved(context: Context): SavedItem? {
        val prefs = prefs(context)
        val title = prefs.getString(KEY_TITLE, null)?.takeIf(String::isNotBlank) ?: return null
        return SavedItem(
            title = title,
            subtitle = prefs.getString(KEY_SUBTITLE, null),
            artworkUrl = prefs.getString(KEY_ARTWORK_URL, null),
            positionMs = prefs.getLong(KEY_POSITION_MS, 0L),
            durationMs = prefs.getLong(KEY_DURATION_MS, 0L),
        )
    }

    /**
     * content:// URI of the saved artwork (served by NuvioCarMediaArtworkProvider), or null until it
     * has been downloaded. The path changes with the image so the car does not show a stale cached one.
     */
    fun artworkContentUri(context: Context): android.net.Uri? {
        val file = artworkFile(context)
        if (!file.exists()) return null
        return android.net.Uri.Builder()
            .scheme(android.content.ContentResolver.SCHEME_CONTENT)
            .authority(context.packageName + ".carmediaart")
            .appendPath("artwork-${file.lastModified()}.png")
            .build()
    }

    internal fun artworkFileForProvider(context: Context): File = artworkFile(context)

    private fun idleCallback(appContext: Context) = object : MediaSession.Callback() {
        override fun onPlay() = showOpenAppPrompt(appContext)

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) = showOpenAppPrompt(appContext)
    }

    /**
     * Video can only play inside the app, and Android blocks a background app from opening its own
     * activity. Following the AAOS media error guidance, show a message with an "Open Nuvio" button
     * whose PendingIntent the car UI launches, then fall back to the paused card.
     */
    private fun showOpenAppPrompt(appContext: Context) {
        val mediaSession = get(appContext)
        val launchIntent = buildLaunchIntent(appContext) ?: return
        val pendingIntent = android.app.PendingIntent.getActivity(
            appContext,
            1,
            launchIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE,
        )
        val appName = appContext.applicationInfo.loadLabel(appContext.packageManager).toString()
        val positionMs = mediaSession.controller.playbackState?.position ?: 0L
        mediaSession.setPlaybackState(
            PlaybackState.Builder()
                .setActions(PlaybackState.ACTION_PLAY or PlaybackState.ACTION_PLAY_FROM_MEDIA_ID)
                .setState(PlaybackState.STATE_ERROR, positionMs, 0f, SystemClock.elapsedRealtime())
                .setErrorMessage("Open $appName to continue watching")
                .setExtras(
                    Bundle().apply {
                        putString(EXTRA_ERROR_RESOLUTION_ACTION_LABEL, "Open $appName")
                        putParcelable(EXTRA_ERROR_RESOLUTION_ACTION_INTENT, pendingIntent)
                    },
                )
                .build(),
        )
        mainHandler.removeCallbacksAndMessages(PROMPT_TOKEN)
        mainHandler.postAtTime(
            {
                if (playerOwner == null && mediaSession.controller.playbackState?.state == PlaybackState.STATE_ERROR) {
                    publishIdlePausedState(mediaSession, positionMs)
                }
            },
            PROMPT_TOKEN,
            SystemClock.uptimeMillis() + OPEN_APP_PROMPT_MS,
        )
    }

    private fun publishIdlePausedState(mediaSession: MediaSession, positionMs: Long) {
        mediaSession.setPlaybackState(
            PlaybackState.Builder()
                .setActions(
                    PlaybackState.ACTION_PLAY or
                        PlaybackState.ACTION_PLAY_PAUSE or
                        PlaybackState.ACTION_PLAY_FROM_MEDIA_ID,
                )
                .setState(PlaybackState.STATE_PAUSED, positionMs.coerceAtLeast(0L), 0f, SystemClock.elapsedRealtime())
                .build(),
        )
    }

    internal fun buildLaunchIntent(context: Context): Intent? =
        context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun artworkFile(context: Context) = File(context.applicationContext.filesDir, ARTWORK_FILE_NAME)
}

package com.JF_Nuvio.features.player

import android.media.browse.MediaBrowser
import android.os.Bundle
import android.service.media.MediaBrowserService

// AAOS fork: makes Nuvio a media source for the car (CarMediaService only accepts apps that
// expose a MediaBrowserService) and refills the shared session from disk when the car binds
// after the app was closed, so the home screen media card keeps showing the last item.
class NuvioCarMediaBrowserService : MediaBrowserService() {
    override fun onCreate() {
        super.onCreate()
        NuvioCarMediaSession.restoreIfIdle(this)
        sessionToken = NuvioCarMediaSession.get(this).sessionToken
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        val wantsRecent = rootHints?.getBoolean(BrowserRoot.EXTRA_RECENT) == true
        val extras = if (wantsRecent) Bundle().apply { putBoolean(BrowserRoot.EXTRA_RECENT, true) } else null
        return BrowserRoot(if (wantsRecent) RECENT_ROOT_ID else ROOT_ID, extras)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowser.MediaItem>>) {
        val items = when (parentId) {
            ROOT_ID -> listOf(NuvioCarMediaSession.continueWatchingFolder())
            RECENT_ROOT_ID, NuvioCarMediaSession.CONTINUE_WATCHING_ID ->
                listOfNotNull(NuvioCarMediaSession.lastPlayedBrowseItem(this))
            else -> emptyList()
        }
        result.sendResult(items.toMutableList())
    }

    private companion object {
        const val ROOT_ID = "nuvio_root"
        const val RECENT_ROOT_ID = "nuvio_recent"
    }
}

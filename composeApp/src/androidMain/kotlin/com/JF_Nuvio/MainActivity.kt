package com.JF_Nuvio

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.JF_Nuvio.core.auth.AuthStorage
import com.JF_Nuvio.core.network.ServerConfigurationStorage
import com.JF_Nuvio.core.diagnostics.SentryInitializer
import com.JF_Nuvio.core.deeplink.handleAppUrl
import com.JF_Nuvio.core.storage.PlatformLocalAccountDataCleaner
import com.JF_Nuvio.core.sync.SyncClientIdentityStorage
import com.JF_Nuvio.features.addons.AddonHttpClientProvider
import com.JF_Nuvio.features.addons.AddonStorage
import com.JF_Nuvio.features.collection.CollectionMobileSettingsStorage
import com.JF_Nuvio.features.collection.CollectionStorage
import com.JF_Nuvio.features.debrid.DebridSettingsStorage
import com.JF_Nuvio.features.downloads.DownloadsLiveStatusPlatform
import com.JF_Nuvio.features.downloads.DownloadsPlatformDownloader
import com.JF_Nuvio.features.downloads.DownloadsStorage
import com.JF_Nuvio.features.library.LibraryDisplaySettingsStorage
import com.JF_Nuvio.features.membership.MemberAssetStorage
import com.JF_Nuvio.features.library.LibraryStorage
import com.JF_Nuvio.features.details.MetaScreenSettingsStorage
import com.JF_Nuvio.features.home.HomeCatalogSettingsStorage
import com.JF_Nuvio.features.mdblist.MdbListSettingsStorage
import com.JF_Nuvio.features.notifications.EpisodeReleaseNotificationPlatform
import com.JF_Nuvio.features.notifications.EpisodeReleaseNotificationsStorage
import com.JF_Nuvio.features.player.PlayerSettingsStorage
import com.JF_Nuvio.features.player.PlayerTrackPreferenceStorage
import com.JF_Nuvio.features.player.ExternalPlayerPlatform
import com.JF_Nuvio.features.player.SubtitleFileCache
import com.JF_Nuvio.features.player.PlayerPictureInPictureManager
import com.JF_Nuvio.features.player.PipRemoteActionReceiver
import com.JF_Nuvio.features.p2p.P2pSettingsStorage
import com.JF_Nuvio.features.p2p.P2pStreamingEngine
import com.JF_Nuvio.features.plugins.PluginStorage
import com.JF_Nuvio.features.profiles.AvatarStorage
import com.JF_Nuvio.features.profiles.ProfilePinCacheStorage
import com.JF_Nuvio.features.profiles.ProfileStorage
import com.JF_Nuvio.features.details.SeasonViewModeStorage
import com.JF_Nuvio.features.search.DiscoverSelectionStorage
import com.JF_Nuvio.features.search.SearchHistoryStorage
import com.JF_Nuvio.features.settings.SentrySettingsStorage
import com.JF_Nuvio.features.settings.AppIconPlatform
import com.JF_Nuvio.features.settings.ThemeSettingsStorage
import com.JF_Nuvio.features.trakt.TraktAuthStorage
import com.JF_Nuvio.features.trakt.TraktCommentsStorage
import com.JF_Nuvio.features.trakt.TraktLibraryStorage
import com.JF_Nuvio.features.trakt.TraktSettingsStorage
import com.JF_Nuvio.features.mdblist.PlatformMdbListAuthPersistence
import com.JF_Nuvio.features.mdblist.PlatformMdbListSyncStorage
import com.JF_Nuvio.features.simkl.SimklAuthStorage
import com.JF_Nuvio.features.simkl.SimklSyncStorage
import com.JF_Nuvio.features.tmdb.TmdbSettingsStorage
import com.JF_Nuvio.features.updater.AndroidAppUpdaterPlatform
import com.JF_Nuvio.core.ui.CardDepthStyleStorage
import com.JF_Nuvio.core.ui.PosterCardStyleStorage
import com.JF_Nuvio.core.poster.CustomPosterUrlStorage
import com.JF_Nuvio.features.watched.WatchedStorage
import com.JF_Nuvio.features.streams.StreamLinkCacheStorage
import com.JF_Nuvio.features.streams.StreamBadgeSettingsStorage
import com.JF_Nuvio.features.streams.BingeGroupCacheStorage
import com.JF_Nuvio.features.watchprogress.ContinueWatchingEnrichmentStorage
import com.JF_Nuvio.features.watchprogress.ContinueWatchingPreferencesStorage
import com.JF_Nuvio.features.watchprogress.ResumePromptStorage
import com.JF_Nuvio.features.watchprogress.WatchProgressStorage

open class MainActivity : AppCompatActivity() {
    private var pipRemoteActionReceiver: PipRemoteActionReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initializePlatform(applicationContext)
        installSplashScreen()
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                scrim = 0xFF020404.toInt(),
            ),
        )
        ThemeSettingsStorage.initialize(applicationContext)
        AppIconPlatform.initialize(applicationContext)
        SentrySettingsStorage.initialize(applicationContext)
        SentryInitializer.start(application)
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(R.color.nuvio_background)
        pipRemoteActionReceiver = PipRemoteActionReceiver.register(this)
        SyncClientIdentityStorage.initialize(applicationContext)
        AddonHttpClientProvider.initialize(applicationContext)
        AddonStorage.initialize(applicationContext)
        AuthStorage.initialize(applicationContext)
        ServerConfigurationStorage.initialize(applicationContext)
        LibraryStorage.initialize(applicationContext)
        WatchedStorage.initialize(applicationContext)
        MetaScreenSettingsStorage.initialize(applicationContext)
        com.JF_Nuvio.features.shuffle.EpisodeShuffleStorage.initialize(applicationContext)
        HomeCatalogSettingsStorage.initialize(applicationContext)
        PlayerSettingsStorage.initialize(applicationContext)
        PlayerTrackPreferenceStorage.initialize(applicationContext)
        P2pSettingsStorage.initialize(applicationContext)
        P2pStreamingEngine.initialize(applicationContext)
        ExternalPlayerPlatform.initialize(applicationContext)
        SubtitleFileCache.initialize(applicationContext)
        ProfileStorage.initialize(applicationContext)
        AvatarStorage.initialize(applicationContext)
        ProfilePinCacheStorage.initialize(applicationContext)
        MemberAssetStorage.initialize(applicationContext)
        DiscoverSelectionStorage.initialize(applicationContext)
        SearchHistoryStorage.initialize(applicationContext)
        SeasonViewModeStorage.initialize(applicationContext)
        PosterCardStyleStorage.initialize(applicationContext)
        CustomPosterUrlStorage.initialize(applicationContext)
        CardDepthStyleStorage.initialize(applicationContext)
        DebridSettingsStorage.initialize(applicationContext)
        TmdbSettingsStorage.initialize(applicationContext)
        MdbListSettingsStorage.initialize(applicationContext)
        TraktAuthStorage.initialize(applicationContext)
        TraktCommentsStorage.initialize(applicationContext)
        TraktLibraryStorage.initialize(applicationContext)
        TraktSettingsStorage.initialize(applicationContext)
        PlatformMdbListAuthPersistence.initialize(applicationContext)
        PlatformMdbListSyncStorage.initialize(applicationContext)
        SimklAuthStorage.initialize(applicationContext)
        SimklSyncStorage.initialize(applicationContext)
        LibraryDisplaySettingsStorage.initialize(applicationContext)
        ContinueWatchingPreferencesStorage.initialize(applicationContext)
        ResumePromptStorage.initialize(applicationContext)
        ContinueWatchingEnrichmentStorage.initialize(applicationContext)
        EpisodeReleaseNotificationsStorage.initialize(applicationContext)
        WatchProgressStorage.initialize(applicationContext)
        StreamLinkCacheStorage.initialize(applicationContext)
        StreamBadgeSettingsStorage.initialize(applicationContext)
        BingeGroupCacheStorage.initialize(applicationContext)
        PluginStorage.initialize(applicationContext)
        CollectionMobileSettingsStorage.initialize(applicationContext)
        CollectionStorage.initialize(applicationContext)
        DownloadsStorage.initialize(applicationContext)
        DownloadsPlatformDownloader.initialize(applicationContext)
        DownloadsLiveStatusPlatform.initialize(applicationContext)
        AndroidAppUpdaterPlatform.initialize(applicationContext)
        PlatformLocalAccountDataCleaner.initialize(applicationContext)
        EpisodeReleaseNotificationPlatform.initialize(applicationContext)
        EpisodeReleaseNotificationPlatform.bindActivity(this)
        handleIncomingAppIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingAppIntent(intent)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        PlayerPictureInPictureManager.onUserLeaveHint(this)
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration,
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        PlayerPictureInPictureManager.onPictureInPictureModeChanged(this, isInPictureInPictureMode)
    }

    override fun onDestroy() {
        EpisodeReleaseNotificationPlatform.unbindActivity(this)
        val receiver = pipRemoteActionReceiver
        if (receiver != null) {
            runCatching { unregisterReceiver(receiver) }
            pipRemoteActionReceiver = null
        }
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (EpisodeReleaseNotificationPlatform.handlePermissionRequestResult(requestCode, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun handleIncomingAppIntent(intent: Intent?) {
        val appUrl = intent?.dataString?.trim().orEmpty()
        if (appUrl.isBlank()) return
        handleAppUrl(appUrl)
    }
}

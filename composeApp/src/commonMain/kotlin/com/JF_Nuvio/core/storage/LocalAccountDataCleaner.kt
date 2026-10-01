package com.JF_Nuvio.core.storage

import com.JF_Nuvio.core.build.AppFeaturePolicy
import com.JF_Nuvio.core.sync.SyncManager
import com.JF_Nuvio.core.sync.ProfileSettingsSync
import com.JF_Nuvio.core.tracking.ensureTrackingProvidersRegistered
import com.JF_Nuvio.features.addons.AddonRepository
import com.JF_Nuvio.features.catalog.CatalogRepository
import com.JF_Nuvio.features.collection.CollectionMobileSettingsRepository
import com.JF_Nuvio.features.collection.CollectionRepository
import com.JF_Nuvio.features.details.MetaDetailsRepository
import com.JF_Nuvio.features.details.MetaScreenSettingsRepository
import com.JF_Nuvio.features.home.HomeCatalogSettingsRepository
import com.JF_Nuvio.features.home.HomeRepository
import com.JF_Nuvio.features.library.LibraryRepository
import com.JF_Nuvio.features.membership.MemberAccessRepository
import com.JF_Nuvio.features.library.LibraryDisplaySettingsRepository
import com.JF_Nuvio.features.notifications.EpisodeReleaseNotificationsRepository
import com.JF_Nuvio.features.player.PlayerLaunchStore
import com.JF_Nuvio.features.player.PlayerSettingsRepository
import com.JF_Nuvio.features.p2p.P2pSettingsRepository
import com.JF_Nuvio.features.plugins.PluginRepository
import com.JF_Nuvio.features.player.SubtitleRepository
import com.JF_Nuvio.features.profiles.ProfileRepository
import com.JF_Nuvio.features.profiles.MAX_PROFILES
import com.JF_Nuvio.features.search.SearchRepository
import com.JF_Nuvio.features.settings.ThemeSettingsRepository
import com.JF_Nuvio.features.streams.StreamContextStore
import com.JF_Nuvio.features.streams.StreamBadgeSettingsRepository
import com.JF_Nuvio.features.streams.StreamLaunchStore
import com.JF_Nuvio.features.streams.StreamsRepository
import com.JF_Nuvio.features.tracking.TrackingProviderRegistry
import com.JF_Nuvio.features.tracking.TrackingSettingsRepository
import com.JF_Nuvio.core.ui.CardDepthStyleRepository
import com.JF_Nuvio.core.ui.PosterCardStyleRepository
import com.JF_Nuvio.features.watchprogress.ContinueWatchingPreferencesRepository
import com.JF_Nuvio.features.watchprogress.ContinueWatchingEnrichmentCache
import com.JF_Nuvio.features.watchprogress.WatchProgressRepository
import com.JF_Nuvio.features.watchprogress.WatchProgressSourceCoordinator
import com.JF_Nuvio.features.watched.WatchedRepository

internal object LocalAccountDataCleaner {
    fun wipe() {
        ensureTrackingProvidersRegistered()
        TrackingProviderRegistry.removeStoredProfiles(1..MAX_PROFILES)
        SyncManager.cancelAccountSync()
        WatchProgressSourceCoordinator.clearLocalState()
        ProfileSettingsSync.clearAccountState()
        ContinueWatchingEnrichmentCache.clearLocalState()
        WatchProgressRepository.clearLocalState()
        WatchedRepository.clearLocalState()
        LibraryRepository.runAccountStorageWipe {
            wipePlatformStorage()
        }

        ProfileRepository.clearInMemory()
        MemberAccessRepository.clearLocalState()
        AddonRepository.clearLocalState()
        if (AppFeaturePolicy.pluginsEnabled) {
            PluginRepository.clearLocalState()
        }
        HomeRepository.clear()
        HomeCatalogSettingsRepository.clearLocalState()
        MetaScreenSettingsRepository.clearLocalState()
        com.JF_Nuvio.features.shuffle.EpisodeShuffleRepository.clearLocalState()
        LibraryRepository.clearLocalState()
        LibraryDisplaySettingsRepository.clearLocalState()
        ContinueWatchingPreferencesRepository.clearLocalState()
        EpisodeReleaseNotificationsRepository.clearLocalState()
        CollectionMobileSettingsRepository.clearLocalState()
        CollectionRepository.clearLocalState()
        ThemeSettingsRepository.clearLocalState()
        PosterCardStyleRepository.clearLocalState()
        CardDepthStyleRepository.clearLocalState()
        TrackingProviderRegistry.clearLocalState()
        TrackingSettingsRepository.clearLocalState()
        PlayerSettingsRepository.clearLocalState()
        StreamBadgeSettingsRepository.clearLocalState()
        P2pSettingsRepository.clearLocalState()
        CatalogRepository.clear()
        StreamsRepository.clear()
        MetaDetailsRepository.clear()
        SearchRepository.reset()
        SubtitleRepository.clear()
        PlayerLaunchStore.clear()
        StreamLaunchStore.clear()
        StreamContextStore.clear()
    }

    internal fun wipePlatformStorage(wipeStorage: () -> Unit = PlatformLocalAccountDataCleaner::wipe) {
        try {
            wipeStorage()
        } finally {
            ContinueWatchingEnrichmentCache.clearLocalState()
        }
    }
}

internal expect object PlatformLocalAccountDataCleaner {
    fun wipe()
}

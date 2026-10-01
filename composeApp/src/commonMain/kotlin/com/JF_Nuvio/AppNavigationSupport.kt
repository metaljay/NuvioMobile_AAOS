package com.JF_Nuvio

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.JF_Nuvio.features.catalog.CatalogRepository
import com.JF_Nuvio.features.collection.CollectionEditorPage
import com.JF_Nuvio.features.collection.CollectionEditorRepository
import com.JF_Nuvio.features.collection.FolderDetailRepository
import com.JF_Nuvio.features.collection.disposeCollectionEditorPage
import com.JF_Nuvio.features.home.MetaPreview
import com.JF_Nuvio.features.library.LibraryItem
import com.JF_Nuvio.features.player.ExternalPlayerPlaybackRequest
import com.JF_Nuvio.features.player.PlayerLaunch
import com.JF_Nuvio.features.player.PlayerLaunchStore
import com.JF_Nuvio.features.player.externalPlaybackSession
import com.JF_Nuvio.features.streams.StreamLaunchStore
import com.JF_Nuvio.features.streams.StreamsRepository
import com.JF_Nuvio.features.watchprogress.ResumePromptRepository
import com.JF_Nuvio.features.watchprogress.WatchProgressRepository
import com.JF_Nuvio.navigation.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal val navigationSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(TabsRoute::class, TabsRoute.serializer())
            subclass(DetailRoute::class, DetailRoute.serializer())
            subclass(PersonDetailRoute::class, PersonDetailRoute.serializer())
            subclass(EntityBrowseRoute::class, EntityBrowseRoute.serializer())
            subclass(SettingsPageRoute::class, SettingsPageRoute.serializer())
            subclass(HomescreenSettingsRoute::class, HomescreenSettingsRoute.serializer())
            subclass(MetaScreenSettingsRoute::class, MetaScreenSettingsRoute.serializer())
            subclass(ContinueWatchingSettingsRoute::class, ContinueWatchingSettingsRoute.serializer())
            subclass(DownloadsRoute::class, DownloadsRoute.serializer())
            subclass(DownloadShowRoute::class, DownloadShowRoute.serializer())
            subclass(AddonsSettingsRoute::class, AddonsSettingsRoute.serializer())
            subclass(PluginsSettingsRoute::class, PluginsSettingsRoute.serializer())
            subclass(AccountSettingsRoute::class, AccountSettingsRoute.serializer())
            subclass(SupportersContributorsSettingsRoute::class, SupportersContributorsSettingsRoute.serializer())
            subclass(LicensesAttributionsSettingsRoute::class, LicensesAttributionsSettingsRoute.serializer())
            subclass(CollectionsRoute::class, CollectionsRoute.serializer())
            subclass(CollectionEditorRoute::class, CollectionEditorRoute.serializer())
            subclass(CollectionEditorPageRoute::class, CollectionEditorPageRoute.serializer())
            subclass(FolderDetailRoute::class, FolderDetailRoute.serializer())
            subclass(StreamRoute::class, StreamRoute.serializer())
            subclass(CatalogRoute::class, CatalogRoute.serializer())
            subclass(PlayerRoute::class, PlayerRoute.serializer())
        }
    }
}

internal fun disposeRouteResources(route: AppRoute) {
    when (route) {
        is StreamRoute -> {
            StreamsRepository.clear()
            StreamLaunchStore.remove(route.launchId)
        }

        is PlayerRoute -> {
            ResumePromptRepository.markPlayerExitedNormally()
            PlayerLaunchStore.remove(route.launchId)
        }

        is CatalogRoute -> {
            CatalogRepository.clear()
            CatalogLaunchStore.remove(route.launchId)
        }

        is CollectionEditorRoute -> CollectionEditorRepository.clear()
        is CollectionEditorPageRoute -> {
            runCatching { CollectionEditorPage.valueOf(route.pageName) }
                .getOrNull()
                ?.let(::disposeCollectionEditorPage)
        }
        is FolderDetailRoute -> FolderDetailRepository.clear()
        else -> Unit
    }
}

internal data class PosterActionTarget(
    val preview: MetaPreview,
    val libraryItem: LibraryItem? = null,
    val libraryListKey: String? = null,
)

internal fun PlayerLaunch.toExternalPlayerPlaybackRequest(): ExternalPlayerPlaybackRequest =
    ExternalPlayerPlaybackRequest(
        sourceUrl = sourceUrl,
        title = title,
        streamTitle = streamTitle,
        sourceHeaders = sourceHeaders,
        resumePositionMs = initialPositionMs,
        durationMs = WatchProgressRepository.progressForVideo(
            videoId = videoId ?: parentMetaId,
            parentMetaId = parentMetaId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
        )?.durationMs?.takeIf { it > 0L },
        playbackSession = externalPlaybackSession(),
        season = seasonNumber,
        episode = episodeNumber,
        episodeTitle = episodeTitle,
    )

package com.JF_Nuvio.features.streams

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.JF_Nuvio.core.build.AppFeaturePolicy
import com.JF_Nuvio.features.addons.AddonManifest
import com.JF_Nuvio.features.addons.AddonRepository
import com.JF_Nuvio.features.addons.ManagedAddon
import com.JF_Nuvio.features.details.MetaDetailsRepository
import com.JF_Nuvio.features.downloads.DownloadsRepository
import com.JF_Nuvio.features.plugins.PluginRepository
import com.JF_Nuvio.features.plugins.PluginsUiState

internal fun AddonManifest.supportsStream(type: String, videoId: String): Boolean =
    resources.any { resource ->
        resource.name == "stream" &&
            resource.types.contains(type) &&
            (resource.idPrefixes.isEmpty() || resource.idPrefixes.any { videoId.startsWith(it) })
    }

internal fun hasCompatiblePlaybackSource(
    addons: List<ManagedAddon>,
    plugins: PluginsUiState,
    type: String,
    videoId: String,
): Boolean = addons.any { it.enabled && it.manifest?.supportsStream(type, videoId) == true } ||
    (plugins.pluginsEnabled && plugins.scrapers.any { it.enabled && it.supportsType(type) })

internal class PlaybackAvailability(
    private val addons: List<ManagedAddon>,
    private val plugins: PluginsUiState,
) {
    fun canStream(type: String, videoId: String): Boolean =
        hasCompatiblePlaybackSource(addons, plugins, type, videoId) ||
            MetaDetailsRepository.findEmbeddedStreams(videoId).isNotEmpty()

    fun canPlay(
        type: String,
        videoId: String,
        parentMetaId: String,
        seasonNumber: Int? = null,
        episodeNumber: Int? = null,
    ): Boolean = canStream(type, videoId) || DownloadsRepository.findPlayableDownload(
        parentMetaId = parentMetaId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        videoId = videoId,
    ) != null

    companion object {
        fun current(): PlaybackAvailability = PlaybackAvailability(
            addons = AddonRepository.uiState.value.addons,
            plugins = if (AppFeaturePolicy.pluginsEnabled) {
                PluginRepository.uiState.value
            } else {
                PluginsUiState(pluginsEnabled = false)
            },
        )
    }
}

@Composable
internal fun rememberPlaybackAvailability(): PlaybackAvailability {
    val addons by remember {
        AddonRepository.initialize()
        AddonRepository.uiState
    }.collectAsStateWithLifecycle()
    val plugins = if (AppFeaturePolicy.pluginsEnabled) {
        val state by remember {
            PluginRepository.initialize()
            PluginRepository.uiState
        }.collectAsStateWithLifecycle()
        state
    } else {
        PluginsUiState(pluginsEnabled = false)
    }
    val downloads by remember {
        DownloadsRepository.ensureLoaded()
        DownloadsRepository.uiState
    }.collectAsStateWithLifecycle()
    return remember(addons, plugins, downloads) {
        PlaybackAvailability(addons.addons, plugins)
    }
}

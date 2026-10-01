package com.JF_Nuvio.core.network

import com.JF_Nuvio.core.build.AppFeaturePolicy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ServerCapabilities(
    val emailPasswordAuth: Boolean,
    val tvLogin: Boolean,
)

data class ServerConfiguration(
    val backendUrl: String,
    val publishableKey: String,
    val capabilities: ServerCapabilities,
    val isCustom: Boolean,
    val discoveryUrl: String? = null,
    val fallbackBackendUrl: String? = null,
) {
    val isSecure: Boolean
        get() = backendUrl.startsWith("https://", ignoreCase = true)

    val isPublicHost: Boolean
        get() = isPublicServerHost(backendUrl)
}

object ServerConfigurationRepository {
    private val _active = MutableStateFlow(loadActiveConfiguration())
    val active: StateFlow<ServerConfiguration> = _active.asStateFlow()

    suspend fun resolveOfficialConfiguration(): ServerConfiguration {
        val current = _active.value
        if (current.isCustom || current.publishableKey.isNotBlank()) return current
        check(ServerDiscoveryPolicy.isCanonicalOfficialBackend(current.backendUrl)) {
            "The official server publishable key is missing and cannot be loaded from this backend."
        }

        val discovered = ServerDiscoveryService.discover(current.backendUrl).getOrThrow()
        check(ServerDiscoveryPolicy.isCanonicalOfficialBackend(discovered.backendUrl)) {
            "The official server discovery document returned an unexpected backend."
        }
        val resolved = discovered.copy(
            isCustom = false,
            fallbackBackendUrl = current.fallbackBackendUrl,
        )
        if (_active.value == current) {
            _active.value = resolved
            SupabaseProvider.reset()
            return resolved
        }
        return _active.value
    }

    fun saveCustom(configuration: ServerConfiguration): Boolean {
        if (!AppFeaturePolicy.customServerConnectionsEnabled) return false
        if (!ServerConfigurationStorage.saveCustom(configuration)) return false
        _active.value = configuration
        return true
    }

    fun useOfficial(): Boolean {
        if (!ServerConfigurationStorage.useOfficial()) return false
        _active.value = officialConfiguration()
        return true
    }

    private fun loadActiveConfiguration(): ServerConfiguration {
        if (!AppFeaturePolicy.customServerConnectionsEnabled) return officialConfiguration()
        return ServerConfigurationStorage.loadCustom() ?: officialConfiguration()
    }
}

internal fun officialConfiguration() = ServerConfiguration(
    backendUrl = officialBackendUrl(SupabaseConfig.URL),
    publishableKey = SupabaseConfig.ANON_KEY.trim(),
    capabilities = ServerCapabilities(
        emailPasswordAuth = true,
        tvLogin = true,
    ),
    isCustom = false,
    fallbackBackendUrl = SupabaseConfig.FALLBACK_URL.trim().trimEnd('/').takeIf { it.isNotBlank() },
)

internal fun officialBackendUrl(configuredUrl: String): String =
    configuredUrl.trim().trimEnd('/').ifBlank { "https://api.nuvio.tv" }

internal fun isPublicServerHost(url: String): Boolean {
    val host = runCatching { io.ktor.http.Url(url).host.lowercase() }.getOrNull() ?: return true
    if (host == "localhost" || host.endsWith(".local") || host == "::1") return false
    if (host.startsWith("127.") || host.startsWith("10.") || host.startsWith("192.168.")) return false
    val parts = host.split('.')
    if (parts.size == 4) {
        val first = parts[0].toIntOrNull()
        val second = parts[1].toIntOrNull()
        if (first == 172 && second != null && second in 16..31) return false
        if (first == 169 && second == 254) return false
    }
    return true
}

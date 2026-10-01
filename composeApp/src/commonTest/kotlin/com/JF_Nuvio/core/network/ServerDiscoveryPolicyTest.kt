package com.JF_Nuvio.core.network

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ServerDiscoveryPolicyTest {
    @Test
    fun `official backend defaults to api nuvio tv when unset`() {
        assertEquals("https://api.nuvio.tv", officialBackendUrl(""))
    }

    @Test
    fun `official API can be reviewed as a discovered server`() {
        assertFalse(ServerDiscoveryPolicy.isOfficial("https://api.nuvio.tv/.well-known/nuvio"))
    }

    @Test
    fun `canonical API is the default official backend`() {
        assertTrue(ServerDiscoveryPolicy.isCanonicalOfficialBackend("https://api.nuvio.tv"))
    }
}

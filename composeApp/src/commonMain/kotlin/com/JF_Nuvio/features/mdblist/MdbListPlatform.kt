package com.JF_Nuvio.features.mdblist

import io.ktor.client.HttpClient

internal expect object PlatformMdbListAuthPersistence : MdbListAuthPersistence {
    override fun read(profileId: Int): String?
    override fun write(profileId: Int, value: String?)
    override fun clear()
}

internal expect fun createMdbListHttpClient(): HttpClient

package com.JF_Nuvio.features.streams

internal expect object StreamLinkCacheStorage {
    fun loadEntry(hashedKey: String): String?
    fun saveEntry(hashedKey: String, payload: String)
    fun removeEntry(hashedKey: String)
}

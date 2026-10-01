package com.JF_Nuvio.features.downloads

internal expect object DownloadsStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}

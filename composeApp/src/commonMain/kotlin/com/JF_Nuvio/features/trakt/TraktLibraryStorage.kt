package com.JF_Nuvio.features.trakt

internal expect object TraktLibraryStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}
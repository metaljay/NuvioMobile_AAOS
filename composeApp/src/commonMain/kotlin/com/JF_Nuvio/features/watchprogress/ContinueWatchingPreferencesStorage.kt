package com.JF_Nuvio.features.watchprogress

internal expect object ContinueWatchingPreferencesStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}

package com.JF_Nuvio.features.watchprogress

internal expect object WatchProgressStorage {
    fun loadPayload(profileId: Int): String?
    fun savePayload(profileId: Int, payload: String)
}

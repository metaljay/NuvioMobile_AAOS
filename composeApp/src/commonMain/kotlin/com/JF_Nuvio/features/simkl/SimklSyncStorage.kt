package com.JF_Nuvio.features.simkl

internal expect object SimklSyncStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
    fun removeProfile(profileId: Int)
}

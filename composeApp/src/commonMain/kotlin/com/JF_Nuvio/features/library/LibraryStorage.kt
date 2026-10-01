package com.JF_Nuvio.features.library

internal expect object LibraryStorage {
    fun loadPayload(profileId: Int): String?
    fun savePayload(profileId: Int, payload: String)
}

package com.JF_Nuvio.features.library

internal expect object LibraryDisplaySettingsStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}

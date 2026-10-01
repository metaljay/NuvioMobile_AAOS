package com.JF_Nuvio.features.details

internal expect object MetaScreenSettingsStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}
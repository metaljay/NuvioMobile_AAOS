package com.JF_Nuvio.features.home

internal expect object HomeCatalogSettingsStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}

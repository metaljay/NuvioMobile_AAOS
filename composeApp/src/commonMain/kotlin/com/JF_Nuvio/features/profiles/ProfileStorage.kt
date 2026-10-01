package com.JF_Nuvio.features.profiles

internal expect object ProfileStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}
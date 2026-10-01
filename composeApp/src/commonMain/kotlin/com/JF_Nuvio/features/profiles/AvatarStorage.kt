package com.JF_Nuvio.features.profiles

internal expect object AvatarStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}
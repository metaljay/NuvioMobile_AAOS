package com.JF_Nuvio.core.ui

internal expect object PosterCardStyleStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}
package com.JF_Nuvio.core.ui

internal expect object CardDepthStyleStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}

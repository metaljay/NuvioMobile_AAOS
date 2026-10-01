package com.JF_Nuvio.features.collection

internal expect object CollectionStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}

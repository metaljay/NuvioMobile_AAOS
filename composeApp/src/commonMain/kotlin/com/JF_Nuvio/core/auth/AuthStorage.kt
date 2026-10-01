package com.JF_Nuvio.core.auth

internal expect object AuthStorage {
    fun loadAnonymousUserId(): String?
    fun saveAnonymousUserId(userId: String)
    fun clearAnonymousUserId()
}

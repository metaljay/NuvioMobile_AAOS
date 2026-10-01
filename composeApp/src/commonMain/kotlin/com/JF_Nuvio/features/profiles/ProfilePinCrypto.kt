package com.JF_Nuvio.features.profiles

internal expect object ProfilePinCrypto {
    fun sha256Hex(value: String): String
}
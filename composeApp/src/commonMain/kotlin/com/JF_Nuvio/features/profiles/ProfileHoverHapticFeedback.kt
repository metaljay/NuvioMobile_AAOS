package com.JF_Nuvio.features.profiles

internal expect object ProfileHoverHapticFeedback {
    fun prepare()
    fun perform()
    fun release()
}

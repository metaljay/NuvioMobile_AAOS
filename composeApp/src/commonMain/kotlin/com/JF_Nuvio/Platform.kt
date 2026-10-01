package com.JF_Nuvio

interface Platform {
    val name: String
    val isAutomotive: Boolean get() = false
}

expect fun getPlatform(): Platform

internal expect val isIos: Boolean

internal expect val supportsPosterNavigationMotion: Boolean

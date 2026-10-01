package com.JF_Nuvio

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class AndroidPlatform(context: Context? = null) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val isAutomotive: Boolean =
        context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE) ?: true
}

private var platformInstance: AndroidPlatform? = null

fun initializePlatform(context: Context) {
    if (platformInstance == null) {
        platformInstance = AndroidPlatform(context)
    }
}

actual fun getPlatform(): Platform = platformInstance ?: AndroidPlatform()

internal actual val isIos: Boolean = false

internal actual val supportsPosterNavigationMotion: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

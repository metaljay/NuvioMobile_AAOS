package com.nuvio.app.core.ui

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import kotlin.math.roundToInt

// AAOS fork-owned file (C5 AAOS readability): applies the Automotive scale (AaosScale.kt) to the
// activity itself, by raising the screen density its resources report. Everything the activity
// draws follows it: the main screen and every dialog, menu, bottom sheet and pop-up window, which
// each read the density from the activity. MainActivity calls this from attachBaseContext.
internal fun Context.withAaosUiScale(): Context {
    val scale = aaosUiScale(packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE))
    if (scale == 1f) return this
    val override = Configuration().apply {
        densityDpi = (resources.configuration.densityDpi * scale).roundToInt()
    }
    return createConfigurationContext(override)
}

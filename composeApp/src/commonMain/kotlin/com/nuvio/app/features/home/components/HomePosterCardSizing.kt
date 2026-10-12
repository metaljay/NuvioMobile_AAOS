package com.nuvio.app.features.home.components

import com.nuvio.app.getPlatform

internal const val AutomotiveHomePosterCardWidthDp = 230

internal fun homePosterCardWidthDp(
    preferenceWidthDp: Int,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Int =
    if (isAutomotive) {
        maxOf(preferenceWidthDp, AutomotiveHomePosterCardWidthDp)
    } else {
        preferenceWidthDp
    }

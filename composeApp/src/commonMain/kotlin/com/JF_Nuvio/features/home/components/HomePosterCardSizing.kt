package com.JF_Nuvio.features.home.components

import com.JF_Nuvio.getPlatform

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

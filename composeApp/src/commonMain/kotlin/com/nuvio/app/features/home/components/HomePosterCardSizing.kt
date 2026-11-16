package com.nuvio.app.features.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.nuvio.app.getPlatform

// AAOS fork-owned file (C5 AAOS readability). Home posters keep their on-car size under the
// 1.5x Automotive scale (AaosScale.kt): 153 dp x 1.5 = about 230 px, five posters per 1280 px row.
// It is a minimum: a larger saved poster width wins.

internal const val AutomotiveHomePosterCardWidthDp = 153

internal fun homePosterCardWidthDp(
    preferenceWidthDp: Int,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Int =
    if (isAutomotive) {
        maxOf(preferenceWidthDp, AutomotiveHomePosterCardWidthDp)
    } else {
        preferenceWidthDp
    }

// Home row cards also keep their on-car label size: the parent's 14 sp title under the 1.5x scale
// would be 21 px and cut off long titles under a 230 px poster; 0.875 brings it back to about 18 px.
internal const val AutomotiveHomeCardFontScale = 0.875f

@Composable
internal fun AaosHomeCardText(enabled: Boolean, content: @Composable () -> Unit) {
    if (!enabled || !getPlatform().isAutomotive) {
        content()
        return
    }
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density.density, AutomotiveHomeCardFontScale),
        content = content,
    )
}

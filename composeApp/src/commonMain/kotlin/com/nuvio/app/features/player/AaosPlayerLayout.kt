package com.nuvio.app.features.player

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuvio.app.getPlatform

// AAOS fork-owned file (C5 AAOS readability). The player and trailer controls use the parent's size
// table (PlayerLayout.kt) under the 1.5x Automotive scale (core/ui/AaosScale.kt). These are the few
// sizes that still came out too small on the car: the play and skip icons keep about their earlier
// on-car size (84 px and 66 px), and the header icons, time text and seek bar get a comfortable minimum.

private val AutomotiveSideIconSize = 56.dp
private val AutomotiveActionButtonSize = 56.dp
private val AutomotiveActionIconSize = 36.dp
private val AutomotiveToolbarIconSize = 32.dp

internal fun PlayerLayoutMetrics.forAutomotive(
    isAutomotive: Boolean = getPlatform().isAutomotive,
): PlayerLayoutMetrics {
    if (!isAutomotive) return this
    return copy(
        headerIconSize = maxOf(headerIconSize, 40.dp),
        playIconSize = maxOf(playIconSize, 72.dp),
        sideButtonPadding = maxOf(sideButtonPadding, 14.dp),
        metadataSize = if (metadataSize.value < 14f) 14.sp else metadataSize,
        timeSize = if (timeSize.value < 14f) 14.sp else timeSize,
        sliderTouchHeight = maxOf(sliderTouchHeight, 32.dp),
    )
}

// The parent draws the skip icons at the play icon's size; on Automotive they stay a step smaller.
internal fun PlayerLayoutMetrics.aaosSideIconSize(
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(sideIconSize, AutomotiveSideIconSize) else playIconSize

// Touch area of the round buttons in the bottom action row and the top toolbar (parent: 48 dp).
internal fun aaosActionButtonSize(
    parent: Dp,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parent, AutomotiveActionButtonSize) else parent

// Icon inside those buttons (parent: 24 dp, 40 dp for the next-episode arrow).
internal fun aaosActionIconSize(
    parent: Dp,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parent, AutomotiveActionIconSize) else parent

// Top toolbar back arrow icon (parent: 24 dp).
internal fun aaosToolbarIconSize(
    parent: Dp,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parent, AutomotiveToolbarIconSize) else parent

// Height reserved for the bottom action row when the gesture overlay is placed (parent: 50 dp).
internal fun aaosActionRowHeight(
    parent: Dp,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parent, AutomotiveActionButtonSize) else parent

package com.nuvio.app.features.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuvio.app.getPlatform

// AAOS fork-owned file (C5 AAOS readability). The parent's PlayerLayout.kt keeps its own size table;
// the player and trailer controls use this one instead: larger at every width, larger again on Automotive.

internal fun PlayerLayoutMetrics.Companion.aaosFromWidth(width: Dp, isAutomotive: Boolean): PlayerLayoutMetrics {
    val metrics = when {
        width >= 1440.dp -> PlayerLayoutMetrics(
            horizontalPadding = 32.dp,
            verticalPadding = 28.dp,
            titleSize = 32.dp.value.sp,
            episodeInfoSize = 20.dp.value.sp,
            metadataSize = 16.dp.value.sp,
            centerGap = 120.dp,
            centerLift = 24.dp,
            sliderBottomOffset = 32.dp,
            sliderTouchHeight = 44.dp,
            sliderScaleY = 0.72f,
            timeSize = 18.dp.value.sp,
            headerIconSize = 36.dp,
            sideButtonPadding = 24.dp,
            sideIconSize = 56.dp,
            playButtonPadding = 32.dp,
            playIconSize = 76.dp,
        )
        width >= 1024.dp -> PlayerLayoutMetrics(
            horizontalPadding = 28.dp,
            verticalPadding = 24.dp,
            titleSize = 28.dp.value.sp,
            episodeInfoSize = 18.dp.value.sp,
            metadataSize = 15.dp.value.sp,
            centerGap = 100.dp,
            centerLift = 20.dp,
            sliderBottomOffset = 28.dp,
            sliderTouchHeight = 40.dp,
            sliderScaleY = 0.74f,
            timeSize = 17.dp.value.sp,
            headerIconSize = 32.dp,
            sideButtonPadding = 22.dp,
            sideIconSize = 50.dp,
            playButtonPadding = 28.dp,
            playIconSize = 68.dp,
        )
        width >= 768.dp -> PlayerLayoutMetrics(
            horizontalPadding = 24.dp,
            verticalPadding = 20.dp,
            titleSize = 26.dp.value.sp,
            episodeInfoSize = 17.dp.value.sp,
            metadataSize = 14.dp.value.sp,
            centerGap = 84.dp,
            centerLift = 16.dp,
            sliderBottomOffset = 24.dp,
            sliderTouchHeight = 38.dp,
            sliderScaleY = 0.78f,
            timeSize = 16.dp.value.sp,
            headerIconSize = 30.dp,
            sideButtonPadding = 20.dp,
            sideIconSize = 46.dp,
            playButtonPadding = 24.dp,
            playIconSize = 60.dp,
        )
        else -> PlayerLayoutMetrics(
            horizontalPadding = 22.dp,
            verticalPadding = 18.dp,
            titleSize = 22.dp.value.sp,
            episodeInfoSize = 16.dp.value.sp,
            metadataSize = 14.dp.value.sp,
            centerGap = 64.dp,
            centerLift = 12.dp,
            sliderBottomOffset = 20.dp,
            sliderTouchHeight = 36.dp,
            sliderScaleY = 0.82f,
            timeSize = 15.dp.value.sp,
            headerIconSize = 28.dp,
            sideButtonPadding = 18.dp,
            sideIconSize = 42.dp,
            playButtonPadding = 22.dp,
            playIconSize = 56.dp,
        )
    }
    if (!isAutomotive) return metrics
    return metrics.copy(
        headerIconSize = metrics.headerIconSize + 12.dp,
        sideButtonPadding = metrics.sideButtonPadding + 4.dp,
        sideIconSize = metrics.sideIconSize + 16.dp,
        playButtonPadding = metrics.playButtonPadding + 4.dp,
        playIconSize = metrics.playIconSize + 16.dp,
    )
}

@Composable
internal fun automotivePlayerLayoutMetrics(width: Dp): PlayerLayoutMetrics =
    PlayerLayoutMetrics.aaosFromWidth(width, isAutomotive = getPlatform().isAutomotive)

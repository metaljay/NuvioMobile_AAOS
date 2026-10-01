package com.JF_Nuvio.features.player

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.JF_Nuvio.getPlatform
import com.JF_Nuvio.isIos
import nuvio.composeapp.generated.resources.Res
import nuvio.composeapp.generated.resources.compose_player_resize_fill
import nuvio.composeapp.generated.resources.compose_player_resize_fit
import nuvio.composeapp.generated.resources.compose_player_resize_zoom
import org.jetbrains.compose.resources.StringResource
import kotlin.math.max

internal data class PlayerLayoutMetrics(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val titleSize: TextUnit,
    val episodeInfoSize: TextUnit,
    val metadataSize: TextUnit,
    val centerGap: Dp,
    val centerLift: Dp,
    val sliderBottomOffset: Dp,
    val sliderTouchHeight: Dp,
    val sliderScaleY: Float,
    val timeSize: TextUnit,
    val headerIconSize: Dp,
    val sideButtonPadding: Dp,
    val sideIconSize: Dp,
    val playButtonPadding: Dp,
    val playIconSize: Dp,
) {
    companion object {
        fun fromWidth(width: Dp, isAutomotive: Boolean = false): PlayerLayoutMetrics {
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
    }
}

@Composable
internal fun automotivePlayerLayoutMetrics(width: Dp): PlayerLayoutMetrics =
    PlayerLayoutMetrics.fromWidth(width, isAutomotive = getPlatform().isAutomotive)

@Composable
internal fun playerHorizontalSafePadding(): Dp {
    val layoutDirection = LocalLayoutDirection.current
    val safePadding = WindowInsets.safeContent.asPaddingValues()
    val left = safePadding.calculateLeftPadding(layoutDirection)
    val right = safePadding.calculateRightPadding(layoutDirection)
    return if (left > right) left else right
}

@Composable
internal fun playerTimelineBottomInsets(metrics: PlayerLayoutMetrics): WindowInsets {
    val safeInsets = WindowInsets.safeContent.only(WindowInsetsSides.Bottom)
    val contentInsets = WindowInsets(bottom = metrics.sliderBottomOffset / 2)
    return if (isIos) safeInsets.union(contentInsets) else safeInsets.add(contentInsets)
}

internal fun PlayerResizeMode.next(): PlayerResizeMode =
    when (this) {
        PlayerResizeMode.Fit -> PlayerResizeMode.Fill
        PlayerResizeMode.Fill -> PlayerResizeMode.Zoom
        PlayerResizeMode.Zoom -> PlayerResizeMode.Fit
    }

internal val PlayerResizeMode.labelRes: StringResource
    get() = when (this) {
        PlayerResizeMode.Fit -> Res.string.compose_player_resize_fit
        PlayerResizeMode.Fill -> Res.string.compose_player_resize_fill
        PlayerResizeMode.Zoom -> Res.string.compose_player_resize_zoom
    }

internal fun formatPlaybackTime(positionMs: Long): String {
    val totalSeconds = (positionMs / 1000L).coerceAtLeast(0L)
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    return if (hours > 0) {
        "${hours}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    } else {
        "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }
}

internal fun formatPlaybackRuntime(positionMs: Long, durationMs: Long, showRemainingTime: Boolean): String =
    if (showRemainingTime) {
        val remainingMs = (durationMs.coerceAtLeast(0L) - positionMs.coerceAtLeast(0L)).coerceAtLeast(0L)
        "−${formatPlaybackTime(remainingMs)}"
    } else {
        "${formatPlaybackTime(positionMs)} / ${formatPlaybackTime(durationMs)}"
    }

internal fun formatPlaybackSpeedLabel(speed: Float): String {
    val normalized = speed.toString().trimEnd('0').trimEnd('.')
    return "${normalized}x"
}

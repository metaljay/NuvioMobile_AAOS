package com.nuvio.app.core.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuvio.app.getPlatform

// AAOS fork-owned file (C5 AAOS readability): the one whole-app scale used on Automotive.
// The Android activity raises its screen density by this (AaosScale.android.kt), so every dp and sp
// in the parent's layouts (text, buttons, menus, dialogs, spacing) is drawn 1.5 times larger on the
// car and unchanged elsewhere. The layouts then see a screen 1.5 times narrower in dp (1280 px wide
// = about 853 dp), so the parent's own breakpoints pick a smaller-tablet layout. Artwork that must
// keep its on-car size is sized separately (HomePosterCardSizing.kt).

internal const val AaosUiScale = 1.5f

internal fun aaosUiScale(isAutomotive: Boolean = getPlatform().isAutomotive): Float =
    if (isAutomotive) AaosUiScale else 1f

// C6 display safe area: the car's bezel covers the outer edge of the window, so touch targets start
// at least 16 dp in on Automotive (16 dp x 1.5 = 24 px on the car screen). Elsewhere the parent's gap stays.
// Pass a larger minimum where the touch target reaches past the padded box (the back arrows do, by 4 dp).
internal fun aaosEdgeGap(
    parentGap: Dp,
    minimum: Dp = AaosMinEdgeGap,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parentGap, minimum) else parentGap

internal val AaosMinEdgeGap = 16.dp

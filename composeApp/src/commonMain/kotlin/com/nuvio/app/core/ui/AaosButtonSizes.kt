package com.nuvio.app.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.nuvio.app.getPlatform

// AAOS fork-owned file (C5 AAOS readability): the shared car button standard, the same in Flow and
// Nuvio. Every icon button is 56 dp to touch (84 px under the 1.5x scale) with a 32 dp icon. These
// helpers only ever raise a size on Automotive; elsewhere they return the parent's own value. They
// cover the back arrows on normal screens and the top navigation pill (the player has its own sizes
// in features/player/AaosPlayerLayout.kt). Chips, list rows and primary buttons are not touched.

internal val AaosButtonTouchSize = 56.dp
internal val AaosButtonIconSize = 32.dp

// Touch size of a back arrow button (parent: 36 to 48 dp depending on the screen).
internal fun aaosButtonTouchSize(
    parent: Dp,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parent, AaosButtonTouchSize) else parent

// Icon inside a button (parent: 24 dp).
internal fun aaosButtonIconSize(
    parent: Dp,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parent, AaosButtonIconSize) else parent

// Height of the top navigation pill (parent: 48 dp when only icons show).
internal fun aaosNavTrackHeight(
    parent: Dp,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parent, AaosButtonTouchSize) else parent

// Icon inside a navigation pill item (parent: 24 dp compact, 28 dp otherwise).
internal fun aaosNavIconSize(
    parent: Dp,
    isAutomotive: Boolean = getPlatform().isAutomotive,
): Dp = if (isAutomotive) maxOf(parent, AaosButtonIconSize) else parent

// For the parent's IconButton back arrows: a fixed 56 dp touch box on Automotive, untouched elsewhere.
internal fun Modifier.aaosButtonTouch(isAutomotive: Boolean = getPlatform().isAutomotive): Modifier =
    if (isAutomotive) size(AaosButtonTouchSize) else this

// For the Icon inside those buttons: 32 dp on Automotive, the parent's default elsewhere.
internal fun Modifier.aaosButtonIcon(isAutomotive: Boolean = getPlatform().isAutomotive): Modifier =
    if (isAutomotive) size(AaosButtonIconSize) else this

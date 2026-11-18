package com.nuvio.app.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class AaosButtonSizesTest {
    @Test
    fun automotiveBackArrowsAre56Touch32Icon() {
        assertEquals(56.dp, aaosButtonTouchSize(40.dp, isAutomotive = true))
        assertEquals(56.dp, aaosButtonTouchSize(48.dp, isAutomotive = true))
        assertEquals(56.dp, aaosButtonTouchSize(36.dp, isAutomotive = true))
        assertEquals(32.dp, aaosButtonIconSize(24.dp, isAutomotive = true))
        assertEquals(32.dp, aaosButtonIconSize(20.dp, isAutomotive = true))
    }

    @Test
    fun automotiveNeverShrinksALargerParentSize() {
        // The player's own 56/40 header button keeps its larger icon.
        assertEquals(40.dp, aaosButtonIconSize(40.dp, isAutomotive = true))
        assertEquals(64.dp, aaosButtonTouchSize(64.dp, isAutomotive = true))
    }

    @Test
    fun automotiveNavPillIs56TallWith32Icons() {
        assertEquals(56.dp, aaosNavTrackHeight(48.dp, isAutomotive = true))
        assertEquals(32.dp, aaosNavIconSize(24.dp, isAutomotive = true))
        assertEquals(32.dp, aaosNavIconSize(28.dp, isAutomotive = true))
    }

    @Test
    fun nonAutomotiveKeepsTheParentSizes() {
        assertEquals(40.dp, aaosButtonTouchSize(40.dp, isAutomotive = false))
        assertEquals(24.dp, aaosButtonIconSize(24.dp, isAutomotive = false))
        assertEquals(48.dp, aaosNavTrackHeight(48.dp, isAutomotive = false))
        assertEquals(24.dp, aaosNavIconSize(24.dp, isAutomotive = false))
        assertEquals(28.dp, aaosNavIconSize(28.dp, isAutomotive = false))
        assertSame(Modifier, Modifier.aaosButtonTouch(isAutomotive = false))
        assertSame(Modifier, Modifier.aaosButtonIcon(isAutomotive = false))
    }
}

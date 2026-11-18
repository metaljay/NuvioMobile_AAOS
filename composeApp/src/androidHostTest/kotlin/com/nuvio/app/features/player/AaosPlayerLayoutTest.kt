package com.nuvio.app.features.player

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AaosPlayerLayoutTest {
    // 1280 px car screen under the 1.5x scale is about 853 dp wide.
    private val parent = PlayerLayoutMetrics.fromWidth(853.dp)

    @Test
    fun nonAutomotiveKeepsTheParentSizes() {
        assertEquals(parent, parent.forAutomotive(isAutomotive = false))
        assertEquals(parent.playIconSize, parent.aaosSideIconSize(isAutomotive = false))
    }

    @Test
    fun automotiveUsesBiggerPlayerButtons() {
        val car = parent.forAutomotive(isAutomotive = true)
        assertEquals(72.dp, car.playIconSize)
        assertEquals(56.dp, car.aaosSideIconSize(isAutomotive = true))
        assertTrue(car.headerIconSize >= 40.dp)
        // Header buttons are the icon plus 16 dp: touch area at least 56 dp.
        assertTrue(car.headerIconSize + 16.dp >= 56.dp)
        assertTrue(car.sliderTouchHeight >= 32.dp)
    }

    @Test
    fun automotiveActionRowAndToolbarAreBigger() {
        assertEquals(56.dp, aaosActionButtonSize(48.dp, isAutomotive = true))
        assertEquals(36.dp, aaosActionIconSize(24.dp, isAutomotive = true))
        assertEquals(40.dp, aaosActionIconSize(40.dp, isAutomotive = true))
        assertEquals(32.dp, aaosToolbarIconSize(24.dp, isAutomotive = true))
        assertEquals(56.dp, aaosActionRowHeight(50.dp, isAutomotive = true))
    }

    @Test
    fun nonAutomotiveKeepsTheParentActionSizes() {
        assertEquals(48.dp, aaosActionButtonSize(48.dp, isAutomotive = false))
        assertEquals(24.dp, aaosActionIconSize(24.dp, isAutomotive = false))
        assertEquals(24.dp, aaosToolbarIconSize(24.dp, isAutomotive = false))
        assertEquals(50.dp, aaosActionRowHeight(50.dp, isAutomotive = false))
    }
}

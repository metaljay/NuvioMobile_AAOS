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
    fun automotiveKeepsPlayAndSkipIconsAtTheirOnCarSize() {
        val car = parent.forAutomotive(isAutomotive = true)
        assertEquals(56.dp, car.playIconSize)
        assertEquals(44.dp, car.aaosSideIconSize(isAutomotive = true))
        assertTrue(car.headerIconSize >= 30.dp)
        assertTrue(car.sliderTouchHeight >= 32.dp)
    }
}

package com.nuvio.app.features.player

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlayerLayoutMetricsTest {
    @Test
    fun automotivePlayerControlsUseLargerIconsAndHitAreas() {
        val standard = PlayerLayoutMetrics.aaosFromWidth(1280.dp, isAutomotive = false)
        val automotive = PlayerLayoutMetrics.aaosFromWidth(1280.dp, isAutomotive = true)

        assertEquals(standard.headerIconSize + 12.dp, automotive.headerIconSize)
        assertEquals(standard.sideIconSize + 16.dp, automotive.sideIconSize)
        assertEquals(standard.playIconSize + 16.dp, automotive.playIconSize)
        assertTrue(automotive.sideButtonPadding > standard.sideButtonPadding)
        assertTrue(automotive.playButtonPadding > standard.playButtonPadding)
    }
}

package com.JF_Nuvio.features.home

import com.JF_Nuvio.features.home.components.AutomotiveHomePosterCardWidthDp
import com.JF_Nuvio.features.home.components.homePosterCardWidthDp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomePosterCardSizingTest {
    @Test
    fun automotiveHomePostersFitFiveAcrossButNotSixAt1280Dp() {
        val width = homePosterCardWidthDp(preferenceWidthDp = 185, isAutomotive = true)
        val fiveCardRow = width * 5 + 16 * 4 + 28 * 2
        val sixCardRow = width * 6 + 16 * 5 + 28 * 2

        assertEquals(AutomotiveHomePosterCardWidthDp, width)
        assertTrue(fiveCardRow <= 1280)
        assertTrue(sixCardRow > 1280)
    }

    @Test
    fun nonAutomotivePostersKeepConfiguredWidth() {
        assertEquals(
            185,
            homePosterCardWidthDp(preferenceWidthDp = 185, isAutomotive = false),
        )
    }
}

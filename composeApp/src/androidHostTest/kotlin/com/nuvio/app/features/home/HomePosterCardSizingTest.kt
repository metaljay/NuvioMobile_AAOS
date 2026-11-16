package com.nuvio.app.features.home

import com.nuvio.app.core.ui.AaosUiScale
import com.nuvio.app.core.ui.aaosUiScale
import com.nuvio.app.features.home.components.AutomotiveHomePosterCardWidthDp
import com.nuvio.app.features.home.components.homePosterCardWidthDp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomePosterCardSizingTest {
    @Test
    fun automotiveScaleIsOneAndAHalfAndOffElsewhere() {
        assertEquals(1.5f, AaosUiScale)
        assertEquals(1.5f, aaosUiScale(isAutomotive = true))
        assertEquals(1f, aaosUiScale(isAutomotive = false))
    }

    @Test
    fun automotiveHomePostersFitFiveAcrossButNotSixOnA1280PxScreen() {
        // The parent's default poster width (126 dp) is below the Automotive minimum.
        val width = homePosterCardWidthDp(preferenceWidthDp = 126, isAutomotive = true)
        val screenWidthDp = 1280 / AaosUiScale
        // Parent's shelf: 10 dp between cards, 24 dp side padding at this width.
        val fiveCardRow = width * 5 + 10 * 4 + 24 * 2
        val sixCardRow = width * 6 + 10 * 5 + 24 * 2

        assertEquals(AutomotiveHomePosterCardWidthDp, width)
        assertTrue(fiveCardRow <= screenWidthDp)
        assertTrue(sixCardRow > screenWidthDp)
    }

    @Test
    fun aLargerSavedPosterWidthWinsOnAutomotive() {
        assertEquals(200, homePosterCardWidthDp(preferenceWidthDp = 200, isAutomotive = true))
    }

    @Test
    fun nonAutomotivePostersKeepConfiguredWidth() {
        assertEquals(126, homePosterCardWidthDp(preferenceWidthDp = 126, isAutomotive = false))
    }
}

package com.JF_Nuvio.features.player

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlaybackLifecyclePolicyTest {
    @Test
    fun pausesWhenActivityLeavesUnlessPlaybackIsInPictureInPicture() {
        assertTrue(shouldPausePlaybackWhenActivityStops(isInPictureInPicture = false, isFinishing = false))
        assertFalse(shouldPausePlaybackWhenActivityStops(isInPictureInPicture = true, isFinishing = false))
        assertTrue(shouldPausePlaybackWhenActivityStops(isInPictureInPicture = true, isFinishing = true))
    }
}

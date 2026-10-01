package com.JF_Nuvio.features.notifications

internal expect object EpisodeReleaseNotificationsClock {
    fun isoDateFromEpochMs(epochMs: Long): String
}
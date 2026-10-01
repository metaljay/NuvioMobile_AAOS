package com.JF_Nuvio.features.notifications

import java.time.Instant
import java.time.ZoneId

internal actual object EpisodeReleaseNotificationsClock {
    actual fun isoDateFromEpochMs(epochMs: Long): String =
        Instant.ofEpochMilli(epochMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .toString()
}
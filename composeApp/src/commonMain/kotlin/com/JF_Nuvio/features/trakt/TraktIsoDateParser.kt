package com.JF_Nuvio.features.trakt

import com.JF_Nuvio.core.time.parseZonedIsoDateTimeToEpochMs

internal fun parseTraktIsoDateTimeToEpochMs(value: String): Long? =
    parseZonedIsoDateTimeToEpochMs(value)

package com.JF_Nuvio.features.watchprogress

expect object CurrentDateProvider {
    fun todayIsoDate(): String
    fun localStartOfDayEpochMs(isoDate: String): Long?
}

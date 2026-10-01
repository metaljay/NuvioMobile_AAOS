package com.JF_Nuvio.features.watchprogress

internal expect object WatchProgressClock {
    fun nowEpochMs(): Long
}

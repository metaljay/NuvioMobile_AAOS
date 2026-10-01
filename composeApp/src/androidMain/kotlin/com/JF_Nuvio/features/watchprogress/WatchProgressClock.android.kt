package com.JF_Nuvio.features.watchprogress

actual object WatchProgressClock {
    actual fun nowEpochMs(): Long = System.currentTimeMillis()
}

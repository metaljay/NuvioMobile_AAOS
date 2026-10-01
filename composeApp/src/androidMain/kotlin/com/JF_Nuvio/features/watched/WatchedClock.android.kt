package com.JF_Nuvio.features.watched

actual object WatchedClock {
    actual fun nowEpochMs(): Long = System.currentTimeMillis()
}


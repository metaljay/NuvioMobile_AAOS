package com.JF_Nuvio.features.library

actual object LibraryClock {
    actual fun nowEpochMs(): Long = System.currentTimeMillis()
}

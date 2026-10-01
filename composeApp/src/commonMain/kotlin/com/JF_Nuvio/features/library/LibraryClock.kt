package com.JF_Nuvio.features.library

internal expect object LibraryClock {
    fun nowEpochMs(): Long
}

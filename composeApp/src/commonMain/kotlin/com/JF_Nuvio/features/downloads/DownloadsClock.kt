package com.JF_Nuvio.features.downloads

internal expect object DownloadsClock {
    fun nowEpochMs(): Long
}

package com.JF_Nuvio.features.downloads

internal actual object DownloadsClock {
    actual fun nowEpochMs(): Long = System.currentTimeMillis()
}

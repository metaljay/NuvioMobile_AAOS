package com.JF_Nuvio.features.downloads

internal expect object DownloadsLiveStatusPlatform {
    fun onItemsChanged(items: List<DownloadItem>)
}

package com.JF_Nuvio.features.search

internal expect object DiscoverSelectionStorage {
    fun loadCatalogKey(): String?
    fun saveCatalogKey(catalogKey: String)
}

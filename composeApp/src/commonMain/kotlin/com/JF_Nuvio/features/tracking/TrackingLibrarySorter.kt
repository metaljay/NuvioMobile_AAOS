package com.JF_Nuvio.features.tracking

import kotlinx.coroutines.flow.Flow

interface TrackingLibrarySorter {
    fun observeAddedOrder(listKey: String, descending: Boolean): Flow<List<String>?>
}

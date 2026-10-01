package com.JF_Nuvio.core.tracking

import com.JF_Nuvio.features.simkl.SimklAuthRepository
import com.JF_Nuvio.features.simkl.SimklMutationRepository
import com.JF_Nuvio.features.simkl.SimklLibraryRepository
import com.JF_Nuvio.features.simkl.SimklProgressRepository
import com.JF_Nuvio.features.simkl.SimklTrackingLibraryProvider
import com.JF_Nuvio.features.simkl.SimklTrackingProgressProvider
import com.JF_Nuvio.features.simkl.SimklWatchedSyncAdapter
import com.JF_Nuvio.features.simkl.SimklSyncRepository
import com.JF_Nuvio.features.tracking.TrackingProviderRegistry
import com.JF_Nuvio.features.mdblist.MdbListTracker
import com.JF_Nuvio.features.trakt.TraktAuthRepository
import com.JF_Nuvio.features.trakt.TraktScrobbleRepository
import com.JF_Nuvio.features.trakt.TraktTrackingLibraryProvider
import com.JF_Nuvio.features.trakt.TraktTrackingProgressProvider
import com.JF_Nuvio.features.watching.sync.TraktWatchedSyncAdapter

fun ensureTrackingProvidersRegistered() {
    MdbListTracker.register()
    TraktAuthRepository.descriptor
    TraktScrobbleRepository.ensureRegistered()
    SimklAuthRepository.descriptor
    SimklSyncRepository.state
    SimklLibraryRepository.uiState
    SimklProgressRepository.uiState
    SimklMutationRepository.ensureRegistered()
    TrackingProviderRegistry.registerLibraryProvider(TraktTrackingLibraryProvider)
    TrackingProviderRegistry.registerLibraryProvider(SimklTrackingLibraryProvider)
    TrackingProviderRegistry.registerWatchedProvider(TraktWatchedSyncAdapter)
    TrackingProviderRegistry.registerWatchedProvider(SimklWatchedSyncAdapter)
    TrackingProviderRegistry.registerProgressProvider(TraktTrackingProgressProvider)
    TrackingProviderRegistry.registerProgressProvider(SimklTrackingProgressProvider)
}

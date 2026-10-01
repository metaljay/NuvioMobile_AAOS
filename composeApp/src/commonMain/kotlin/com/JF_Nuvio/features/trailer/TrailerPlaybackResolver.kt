package com.JF_Nuvio.features.trailer

expect object TrailerPlaybackResolver {
    suspend fun resolveFromYouTubeUrl(youtubeUrl: String): TrailerPlaybackSource?
}

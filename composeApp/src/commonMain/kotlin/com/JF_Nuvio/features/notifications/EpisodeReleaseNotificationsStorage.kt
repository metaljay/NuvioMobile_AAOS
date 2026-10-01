package com.JF_Nuvio.features.notifications

internal expect object EpisodeReleaseNotificationsStorage {
    fun loadPayload(): String?
    fun savePayload(payload: String)
}
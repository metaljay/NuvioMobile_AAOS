package com.JF_Nuvio.features.details

import com.JF_Nuvio.core.storage.ProfileScopedKey
import platform.Foundation.NSUserDefaults

internal actual object MetaScreenSettingsStorage {
    private const val payloadKey = "meta_screen_settings_payload"

    actual fun loadPayload(): String? =
        NSUserDefaults.standardUserDefaults.stringForKey(ProfileScopedKey.of(payloadKey))

    actual fun savePayload(payload: String) {
        NSUserDefaults.standardUserDefaults.setObject(payload, forKey = ProfileScopedKey.of(payloadKey))
    }
}
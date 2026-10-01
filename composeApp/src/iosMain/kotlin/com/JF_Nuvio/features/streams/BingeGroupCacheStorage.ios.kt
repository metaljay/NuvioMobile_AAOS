package com.JF_Nuvio.features.streams

import com.JF_Nuvio.core.storage.ProfileScopedKey
import platform.Foundation.NSUserDefaults

actual object BingeGroupCacheStorage {
    actual fun load(hashedKey: String): String? =
        NSUserDefaults.standardUserDefaults.stringForKey(ProfileScopedKey.of(hashedKey))

    actual fun save(hashedKey: String, value: String) {
        NSUserDefaults.standardUserDefaults.setObject(value, forKey = ProfileScopedKey.of(hashedKey))
    }

    actual fun remove(hashedKey: String) {
        NSUserDefaults.standardUserDefaults.removeObjectForKey(ProfileScopedKey.of(hashedKey))
    }
}

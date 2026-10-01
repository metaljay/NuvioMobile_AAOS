package com.JF_Nuvio.core.network

internal expect object ServerConfigurationStorage {
    fun loadCustom(): ServerConfiguration?
    fun saveCustom(configuration: ServerConfiguration): Boolean
    fun useOfficial(): Boolean
}

package com.willfp.ecoenchants.data.storage

import java.util.*

interface DataHandler {
    fun save()

    fun <T> write(uuid: UUID, key: String, value: T)
    fun <T> read(uuid: UUID, key: String): T?
    fun <T : Any> read(uuid: UUID, key: String, default: T): T {
        return read<T>(uuid, key) ?: default
    }
}
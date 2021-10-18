package com.willfp.ecoenchants.data.storage

import com.willfp.ecoenchants.EcoEnchantsPlugin
import org.bukkit.OfflinePlayer
import java.util.*

@Suppress("UNCHECKED_CAST")
class PlayerProfile private constructor(
    private val data: MutableMap<String, Any>
) {
    fun <T : Any> write(key: String, value: T) {
        data[key] = value
    }

    fun <T : Any> read(key: String, default: T): T {
        return data[key] as T? ?: default
    }

    companion object {
        private val handler = EcoEnchantsPlugin.getInstance().dataHandler
        private val loaded = mutableMapOf<UUID, PlayerProfile>()
        private val keys = mutableMapOf<String, Type>()

        private fun load(uuid: UUID): PlayerProfile {
            val found = loaded[uuid]
            if (found != null) {
                return found
            }

            val data = mutableMapOf<String, Any>()
            for ((key, type) in keys) {
                when (type) {
                    Type.INT -> data[key] = handler.read(uuid, key, 0)
                    Type.DOUBLE -> data[key] = handler.read(uuid, key, 0.0)
                    Type.STRING -> data[key] = handler.read(uuid, key, "Unknown")
                    Type.BOOLEAN -> data[key] = handler.read(uuid, key, false)
                }
            }

            val profile = PlayerProfile(data)
            loaded[uuid] = profile
            return profile
        }

        fun saveAll(async: Boolean) {
            val saver = {
                for ((uuid, profile) in loaded) {
                    for ((key, type) in keys) {
                        when (type) {
                            Type.INT -> handler.write(uuid, key, profile.read(key, 0))
                            Type.DOUBLE -> handler.write(uuid, key, profile.read(key, 0.0))
                            Type.STRING -> handler.write(uuid, key, profile.read(key, "Unknown Value"))
                            Type.BOOLEAN -> handler.write(uuid, key, profile.read(key, false))
                        }
                    }
                }

                handler.save()
            }

            if (async) {
                EcoEnchantsPlugin.getInstance().scheduler.runAsync(saver)
            } else {
                saver.invoke()
            }
        }

        @JvmStatic
        val OfflinePlayer.profile: PlayerProfile
            get() {
                return load(this.uniqueId)
            }

        init {
            keys["descriptions"] = Type.BOOLEAN
        }
    }

    private enum class Type {
        STRING,
        DOUBLE,
        BOOLEAN,
        INT
    }
}
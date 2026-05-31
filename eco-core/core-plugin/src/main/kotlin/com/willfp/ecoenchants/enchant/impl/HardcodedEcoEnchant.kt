package com.willfp.ecoenchants.enchant.impl

import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.config.readConfig
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.ecoenchants.plugin
import com.willfp.libreforge.effects.emptyEffectList
import java.io.File

abstract class HardcodedEcoEnchant(
    id: String,
) : EcoEnchantBase(id, plugin) {
    private val file: File?
        get() = filesById[id]

    val isPresent: Boolean
        get() = file != null

    final override fun loadConfig(): Config {
        return requireNotNull(file) {
            "Could not find hardcoded enchant config for $id"
        }.readConfig(ConfigType.YAML)
    }

    override fun createLevel(level: Int): EcoEnchantLevel {
        return EcoEnchantLevel(this, level, emptyEffectList(), conditions)
    }

    companion object {
        private var filesById = emptyMap<String, File>()

        internal fun reload() {
            val enchantsFolder = File(plugin.dataFolder, "enchants")
            filesById = if (enchantsFolder.exists()) {
                enchantsFolder.walk()
                    .filter { it.isFile }
                    .associateBy { it.nameWithoutExtension }
            } else {
                emptyMap()
            }
        }
    }
}

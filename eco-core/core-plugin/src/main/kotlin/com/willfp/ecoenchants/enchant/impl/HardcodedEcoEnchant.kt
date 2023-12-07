package com.willfp.ecoenchants.enchant.impl

import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.config.readConfig
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.libreforge.effects.emptyEffectList
import java.io.File

abstract class HardcodedEcoEnchant(
    id: String,
    plugin: EcoEnchantsPlugin
) : EcoEnchantBase(id, plugin) {
    private val file: File?
        get() = File(plugin.dataFolder, "enchants")
            .walk()
            .firstOrNull { file -> file.nameWithoutExtension == id }

    val isPresent = file != null

    final override fun loadConfig(): Config {
        return file.readConfig(ConfigType.YAML)
    }

    override fun createLevel(level: Int): EcoEnchantLevel {
        return EcoEnchantLevel(this, level, emptyEffectList(), conditions, plugin)
    }
}

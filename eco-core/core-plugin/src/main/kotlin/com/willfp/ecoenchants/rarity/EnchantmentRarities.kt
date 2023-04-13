package com.willfp.ecoenchants.rarity

import com.willfp.eco.core.registry.Registry
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.RaritySorter

@Suppress("UNUSED")
object EnchantmentRarities : Registry<EnchantmentRarity>() {
    @JvmStatic
    fun update(plugin: EcoEnchantsPlugin) {
        clear()

        for (config in plugin.rarityYml.getSubsections("rarities")) {
            register(EnchantmentRarity(config))
        }

        RaritySorter.update(plugin)
    }
}

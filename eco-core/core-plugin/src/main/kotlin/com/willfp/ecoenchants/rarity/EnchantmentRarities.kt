package com.willfp.ecoenchants.rarity

import com.willfp.eco.core.registry.Registry
import com.willfp.ecoenchants.display.RaritySorter
import com.willfp.ecoenchants.plugin

@Suppress("UNUSED")
object EnchantmentRarities : Registry<EnchantmentRarity>() {
    @JvmStatic
    fun update() {
        clear()

        for (config in plugin.rarityYml.getSubsections("rarities")) {
            register(EnchantmentRarity(config))
        }

        RaritySorter.update()
    }
}

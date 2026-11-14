package com.willfp.ecoenchants.rarity

import com.willfp.ecoenchants.display.RaritySorter
import com.willfp.ecoenchants.plugin
import wiki.ecohub.core.registry.Registry

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

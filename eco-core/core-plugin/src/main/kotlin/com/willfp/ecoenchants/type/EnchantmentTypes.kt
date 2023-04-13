package com.willfp.ecoenchants.type

import com.willfp.eco.core.registry.Registry
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.TypeSorter

@Suppress("UNUSED")
object EnchantmentTypes: Registry<EnchantmentType>() {
    /**
     * Update all [EnchantmentType]s.
     *
     * @param plugin Instance of EcoEnchants.
     */
    fun update(plugin: EcoEnchantsPlugin) {
        for (type in values()) {
            clear()
        }

        for (config in plugin.typesYml.getSubsections("types")) {
            register(EnchantmentType(plugin, config))
        }

        TypeSorter.update(plugin)
    }
}

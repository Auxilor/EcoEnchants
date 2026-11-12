package com.willfp.ecoenchants.type

import com.willfp.ecoenchants.display.TypeSorter
import com.willfp.ecoenchants.plugin
import wiki.ecohub.core.registry.Registry

@Suppress("UNUSED")
object EnchantmentTypes : Registry<EnchantmentType>() {
    /**
     * Update all [EnchantmentType]s.
     *
     * @param plugin Instance of EcoEnchants.
     */
    fun update() {
        for (type in values()) {
            clear()
        }

        for (config in plugin.typesYml.getSubsections("types")) {
            register(EnchantmentType(config))
        }

        TypeSorter.update()
    }
}

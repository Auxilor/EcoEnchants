package com.willfp.ecoenchants.type

import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableSet
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.target.EnchantmentTargets

@Suppress("UNUSED")
object EnchantmentTypes {
    private val BY_ID = HashBiMap.create<String, EnchantmentType>()

    /**
     * Get all registered [EnchantmentType]s.
     *
     * @return A list of all [EnchantmentType]s.
     */
    @JvmStatic
    fun values(): Set<EnchantmentType> {
        return ImmutableSet.copyOf(BY_ID.values)
    }

    /**
     * Get [String]s for all registered [EnchantmentType]s.
     *
     * @return A list of all [EnchantmentType]s.
     */
    @JvmStatic
    fun keySet(): Set<String> {
        return ImmutableSet.copyOf(BY_ID.keys)
    }

    /**
     * Get [EnchantmentType] matching key.
     *
     * @param id The key to search for.
     * @return The matching [EnchantmentType], or null if not found.
     */
    @JvmStatic
    fun getByID(id: String?): EnchantmentType? {
        return if (id == null) {
            null
        } else BY_ID[id]
    }

    /**
     * Update all [EnchantmentType]s.
     *
     * @param plugin Instance of EcoEnchants.
     */
    @JvmStatic
    fun update(plugin: EcoEnchantsPlugin) {
        for (type in values()) {
            removeType(type)
        }
        for (config in plugin.typesYml.getSubsections("types")) {
            EnchantmentType(config)
        }
    }

    /**
     * Remove [EnchantmentType] from EcoEnchants.
     *
     * @param type The [EnchantmentType] to remove.
     */
    @JvmStatic
    fun removeType(type: EnchantmentType) {
        BY_ID.remove(type.id)
    }

    /**
     * Add new [EnchantmentType] to EcoEnchants.
     *
     * Only for internal use, types are automatically added in the constructor.
     *
     * @param type The [EnchantmentType] to add.
     */
    internal fun addNewType(type: EnchantmentType) {
        BY_ID.remove(type.id)
        BY_ID[type.id] = type
    }
}

package com.willfp.ecoenchants.rarity

import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableSet
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchants

@Suppress("UNUSED")
object EnchantmentRarities {
    private val BY_ID = HashBiMap.create<String, EnchantmentRarity>()

    /**
     * Get all registered [EnchantmentRarity]s.
     *
     * @return A list of all [EnchantmentRarity]s.
     */
    @JvmStatic
    fun values(): Set<EnchantmentRarity> {
        return ImmutableSet.copyOf(BY_ID.values)
    }

    /**
     * Get [String]s for all registered [EnchantmentRarity]s.
     *
     * @return A list of all [EnchantmentRarity]s.
     */
    @JvmStatic
    fun keySet(): Set<String> {
        return ImmutableSet.copyOf(BY_ID.keys)
    }

    /**
     * Get [EnchantmentRarity] matching key.
     *
     * @param id The key to search for.
     * @return The matching [EnchantmentRarity], or null if not found.
     */
    @JvmStatic
    fun getByID(id: String?): EnchantmentRarity? {
        return if (id == null) {
            null
        } else BY_ID[id]
    }

    /**
     * Update all [EnchantmentRarity]s.
     *
     * @param plugin Instance of EcoEnchants.
     */
    @JvmStatic
    fun update(plugin: EcoEnchantsPlugin) {
        for (type in values()) {
            removeRarity(type)
        }
        for (config in plugin.rarityYml.getSubsections("rarities")) {
            EnchantmentRarity(config)
        }
    }

    /**
     * Remove [EnchantmentRarity] from EcoEnchants.
     *
     * @param rarity The [EnchantmentRarity] to remove.
     */
    @JvmStatic
    fun removeRarity(rarity: EnchantmentRarity) {
        BY_ID.remove(rarity.id)
    }

    /**
     * Add new [EnchantmentRarity] to EcoEnchants.
     *
     * Only for internal use, rarities are automatically added in the constructor.
     *
     * @param rarity The [EnchantmentRarity] to add.
     */
    internal fun addNewRarity(rarity: EnchantmentRarity) {
        BY_ID.remove(rarity.id)
        BY_ID[rarity.id] = rarity
    }
}

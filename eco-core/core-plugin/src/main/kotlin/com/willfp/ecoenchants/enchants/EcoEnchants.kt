package com.willfp.ecoenchants.enchants

import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableSet
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.impl.EnchantmentPermanenceCurse
import com.willfp.ecoenchants.enchants.impl.EnchantmentRepairing
import com.willfp.ecoenchants.enchants.impl.EnchantmentReplenish
import com.willfp.ecoenchants.enchants.impl.EnchantmentSoulbound
import com.willfp.ecoenchants.integrations.EnchantRegistrations
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.target.EnchantmentTargets
import com.willfp.ecoenchants.type.EnchantmentTypes
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

@Suppress("UNUSED")
object EcoEnchants : ConfigCategory("enchant", "enchants") {
    private val BY_KEY = HashBiMap.create<String, EcoEnchant>()
    private val BY_NAME = HashBiMap.create<String, EcoEnchant>()

    override val shouldPreload = true

    /**
     * Get all registered [EcoEnchant]s.
     *
     * @return A list of all [EcoEnchant]s.
     */
    @JvmStatic
    fun values(): Set<EcoEnchant> {
        return ImmutableSet.copyOf(BY_KEY.values)
    }

    /**
     * Get [String]s for all registered [EcoEnchant]s.
     *
     * @return A list of all [EcoEnchant]s.
     */
    @JvmStatic
    fun keySet(): Set<String> {
        return ImmutableSet.copyOf(BY_KEY.keys)
    }

    /**
     * Get [EcoEnchant] matching id.
     *
     * @param id The id to search for.
     * @return The matching [EcoEnchant], or null if not found.
     */
    @JvmStatic
    fun getByID(id: String?): EcoEnchant? {
        return if (id == null) {
            null
        } else BY_KEY[id]
    }

    /**
     * Get [EcoEnchant] matching key.
     *
     * @param key The key to search for.
     * @return The matching [EcoEnchant], or null if not found.
     */
    @JvmStatic
    fun getByKey(key: NamespacedKey?): EcoEnchant? {
        return if (key == null) {
            null
        } else getByID(key.key)
    }

    /**
     * Get [EcoEnchant] matching name.
     *
     * @param name The name to search for.
     * @return The matching [EcoEnchant], or null if not found.
     */
    @JvmStatic
    fun getByName(name: String?): EcoEnchant? {
        return if (name == null) {
            null
        } else BY_NAME[name]
    }


    override fun clear(plugin: LibreforgePlugin) {
        for (enchant in values()) {
            removeEnchant(enchant)

            for (listener in enchant.listeners) {
                plugin.eventManager.unregisterListener(listener)
            }
        }
    }

    override fun beforeReload(plugin: LibreforgePlugin) {
        plugin as EcoEnchantsPlugin

        EnchantmentRarities.update(plugin)
        EnchantmentTargets.update(plugin)
        EnchantmentTypes.update(plugin)
    }

    override fun afterReload(plugin: LibreforgePlugin) {
        plugin as EcoEnchantsPlugin

        sendPrompts(plugin)
        registerHardcodedEnchantments(plugin)
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        plugin as EcoEnchantsPlugin

        if (config.has("effects")) {
            try {
                LibReforgeEcoEnchant(
                    id,
                    config,
                    plugin
                )
            } catch (e: MissingDependencyException) {
                addPluginPrompt(plugin, e.plugins)
            }
        }
    }

    /**
     * Remove [Enchantment] from EcoEnchants.
     *
     * @param enchant The [Enchantment] to remove.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    fun removeEnchant(enchant: Enchantment) {
        if (enchant is EcoEnchant) {
            BY_KEY.remove(enchant.id)
            BY_NAME.remove(ChatColor.stripColor(enchant.displayName))
            EnchantRegistrations.removeEnchant(enchant)
        }

        Enchantment::class.java.getDeclaredField("byKey")
            .apply {
                isAccessible = true
                (get(null) as MutableMap<NamespacedKey, Enchantment>).apply { remove(enchant.key) }
            }

        Enchantment::class.java.getDeclaredField("byName")
            .apply {
                isAccessible = true
                (get(null) as MutableMap<String, Enchantment>).apply { remove(enchant.name) }
            }
    }

    /**
     * Add new [EcoEnchant] to EcoEnchants.
     *
     * Only for internal use, enchants are automatically added in the
     * constructor.
     *
     * @param enchant The [EcoEnchant] to add.
     */
    internal fun addNewEnchant(enchant: EcoEnchant) {
        register(enchant)

        BY_KEY[enchant.id] = enchant
        BY_NAME[ChatColor.stripColor(enchant.displayName)] = enchant
    }

    /**
     * Register a new [Enchantment] with the server.
     *
     * @param enchantment The [Enchantment] to add.
     */
    @JvmStatic
    fun register(enchantment: Enchantment) {
        Enchantment::class.java.getDeclaredField("acceptingNew")
            .apply {
                isAccessible = true
                set(null, true)
            }

        removeEnchant(enchantment)

        Enchantment.registerEnchantment(enchantment)
        EnchantRegistrations.registerEnchantments()
    }

    /** Register the hardcoded enchantments. */
    private fun registerHardcodedEnchantments(
        plugin: EcoEnchantsPlugin
    ) {
        EnchantmentPermanenceCurse(plugin)
        EnchantmentRepairing(plugin)
        EnchantmentReplenish(plugin)
        EnchantmentSoulbound(plugin)
    }
}

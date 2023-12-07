package com.willfp.ecoenchants.enchants

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentPermanenceCurse
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentRepairing
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentReplenish
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentSoulbound
import com.willfp.ecoenchants.integrations.EnchantRegistrations
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.target.EnchantmentTargets
import com.willfp.ecoenchants.type.EnchantmentTypes
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.RegistrableCategory
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

@Suppress("UNUSED")
object EcoEnchants : RegistrableCategory<EcoEnchant>("enchant", "enchants") {
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
            registry.remove(enchant.id)
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

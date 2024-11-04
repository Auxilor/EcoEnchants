package com.willfp.ecoenchants.enchant

import com.google.common.collect.HashBiMap
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.impl.EcoEnchantBase
import com.willfp.ecoenchants.enchant.impl.LibreforgeEcoEnchant
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentPermanenceCurse
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentRepairing
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentReplenish
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentSoulbound
import com.willfp.ecoenchants.enchant.registration.modern.ModernEnchantmentRegistererProxy
import com.willfp.ecoenchants.integrations.EnchantRegistrations
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.target.EnchantmentTargets
import com.willfp.ecoenchants.type.EnchantmentTypes
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.RegistrableCategory
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

@Suppress("UNUSED")
object EcoEnchants : RegistrableCategory<EcoEnchant>("enchant", "enchants") {
    private val BY_NAME = HashBiMap.create<String, EcoEnchant>()

    override val shouldPreload = true

    override fun clear(plugin: LibreforgePlugin) {
        plugin as EcoEnchantsPlugin

        for (enchant in registry.values()) {
            plugin.enchantmentRegisterer.unregister(enchant)
            EnchantRegistrations.removeEnchant(enchant)
            BY_NAME.remove(enchant.getFormattedName(0))
        }

        registry.clear()
    }

    override fun beforeReload(plugin: LibreforgePlugin) {
        // Replace registry on reload to manage some enchantment removal logic
        if (Prerequisite.HAS_1_20_3.isMet) {
            plugin.getProxy(ModernEnchantmentRegistererProxy::class.java).replaceRegistry()
        }

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

    override fun acceptPreloadConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        plugin as EcoEnchantsPlugin

        if (!config.has("effects")) {
            return
        }

        try {
            val enchant = LibreforgeEcoEnchant(
                id,
                config,
                plugin
            )

            doRegister(plugin, enchant)
        } catch (e: MissingDependencyException) {
            // Ignore missing dependencies for preloaded enchants
        }
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        plugin as EcoEnchantsPlugin

        if (!config.has("effects")) {
            return
        }

        try {
            val enchant = LibreforgeEcoEnchant(
                id,
                config,
                plugin
            )

            doRegister(plugin, enchant)
        } catch (e: MissingDependencyException) {
            addPluginPrompt(plugin, e.plugins)
        }
    }

    private fun doRegister(plugin: EcoEnchantsPlugin, enchant: EcoEnchantBase) {
        val enchantment = plugin.enchantmentRegisterer.register(enchant)
        // Register delegated versions
        registry.register(enchantment as EcoEnchant)
        BY_NAME[ChatColor.stripColor(enchant.getFormattedName(0))] = enchantment as EcoEnchant
        EnchantRegistrations.registerEnchantments()
    }

    private fun registerHardcodedEnchantments(
        plugin: EcoEnchantsPlugin
    ) {
        val hardcodedEnchantments = listOf(
            EnchantmentPermanenceCurse(plugin),
            EnchantmentRepairing(plugin),
            EnchantmentReplenish(plugin),
            EnchantmentSoulbound(plugin)
        )

        for (enchantment in hardcodedEnchantments) {
            // Only register if not already registered (so hardcode can be overridden)
            if (enchantment.isPresent && registry[enchantment.id] == null) {
                doRegister(plugin, enchantment)
            }
        }
    }

    fun getByName(name: String?): EcoEnchant? {
        return if (name == null) {
            null
        } else BY_NAME[name]
    }
}

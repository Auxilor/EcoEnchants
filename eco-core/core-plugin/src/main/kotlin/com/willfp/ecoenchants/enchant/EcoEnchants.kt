package com.willfp.ecoenchants.enchant

import com.google.common.collect.HashBiMap
import com.google.common.collect.Maps
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.enchant.CustomEnchantments
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.impl.EcoEnchantBase
import com.willfp.ecoenchants.enchant.impl.LibreforgeEcoEnchant
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentPermanenceCurse
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentRepairing
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentReplenish
import com.willfp.ecoenchants.enchant.impl.hardcoded.EnchantmentSoulbound
import com.willfp.ecoenchants.integrations.EnchantRegistrations
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.target.EnchantmentTargets
import com.willfp.ecoenchants.type.EnchantmentTypes
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.RegistrableCategory
import org.bukkit.ChatColor

@Suppress("UNUSED")
object EcoEnchants : RegistrableCategory<EcoEnchant>("enchant", "enchants") {
    private val BY_NAME = Maps.synchronizedBiMap(HashBiMap.create<String, EcoEnchant>())

    override val shouldPreload = true

    override fun clear(plugin: LibreforgePlugin) {
        for (enchant in registry.values()) {
            CustomEnchantments.unregister(enchant)
            EnchantRegistrations.removeEnchant(enchant)
            BY_NAME.remove(ChatColor.stripColor(enchant.getFormattedName(0))?.lowercase())
        }

        registry.clear()
        ecoEnchantLikes.clear()
    }

    override fun beforeReload(plugin: LibreforgePlugin) {
        CustomEnchantments.unfreezeRegistry()

        EnchantmentRarities.update()
        EnchantmentTargets.update()
        EnchantmentTypes.update()
    }

    override fun afterReload(plugin: LibreforgePlugin) {
        sendPrompts()
        registerHardcodedEnchantments()

        CustomEnchantments.freezeRegistry()
    }

    override fun acceptPreloadConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        plugin as EcoEnchantsPlugin

        if (!config.has("effects")) {
            return
        }

        try {
            val enchant = LibreforgeEcoEnchant(
                id,
                config
            )

            doRegister(enchant)
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
            )

            doRegister(enchant)
        } catch (e: MissingDependencyException) {
            addPluginPrompt(plugin, e.plugins)
        }
    }

    private fun doRegister(enchant: EcoEnchantBase) {
        enchant.enchantment = CustomEnchantments.register(enchant)
        registry.register(enchant)
        @Suppress("DEPRECATION")
        BY_NAME[ChatColor.stripColor(enchant.getFormattedName(0))?.lowercase()] = enchant
        EnchantRegistrations.registerEnchantments()
    }

    private fun registerHardcodedEnchantments() {
        val hardcodedEnchantments = listOf(
            EnchantmentPermanenceCurse,
            EnchantmentRepairing,
            EnchantmentReplenish,
            EnchantmentSoulbound
        )

        for (enchantment in hardcodedEnchantments) {
            // Only register if not already registered (so hardcode can be overridden)
            if (enchantment.isPresent && registry[enchantment.id] == null) {
                doRegister(enchantment)
            }
        }
    }

    fun getByName(name: String?): EcoEnchant? {
        return if (name == null) {
            null
        } else BY_NAME[name.lowercase()]
    }
}

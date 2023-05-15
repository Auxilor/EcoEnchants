package com.willfp.ecoenchants

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.integrations.IntegrationLoader
import com.willfp.ecoenchants.commands.CommandEcoEnchants
import com.willfp.ecoenchants.commands.CommandEnchantInfo
import com.willfp.ecoenchants.config.RarityYml
import com.willfp.ecoenchants.config.TargetsYml
import com.willfp.ecoenchants.config.TypesYml
import com.willfp.ecoenchants.config.VanillaEnchantsYml
import com.willfp.ecoenchants.display.DisplayCache
import com.willfp.ecoenchants.display.EnchantDisplay
import com.willfp.ecoenchants.display.EnchantSorter
import com.willfp.ecoenchants.enchants.EcoEnchantLevel
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.ecoenchants.enchants.EnchantGUI
import com.willfp.ecoenchants.enchants.LoreConversion
import com.willfp.ecoenchants.enchants.registerVanillaEnchants
import com.willfp.ecoenchants.integrations.EnchantRegistrations
import com.willfp.ecoenchants.integrations.plugins.CMIIntegration
import com.willfp.ecoenchants.integrations.plugins.EssentialsIntegration
import com.willfp.ecoenchants.mechanics.AnvilSupport
import com.willfp.ecoenchants.mechanics.EnchantingTableSupport
import com.willfp.ecoenchants.mechanics.ExtraItemSupport
import com.willfp.ecoenchants.mechanics.GrindstoneSupport
import com.willfp.ecoenchants.mechanics.LootSupport
import com.willfp.ecoenchants.mechanics.VillagerSupport
import com.willfp.ecoenchants.target.EnchantLookup.clearEnchantCache
import com.willfp.ecoenchants.target.EnchantLookup.heldEnchantLevels
import com.willfp.libreforge.NamedValue
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.libreforge.registerHolderPlaceholderProvider
import com.willfp.libreforge.registerHolderProvider
import com.willfp.libreforge.registerPlayerRefreshFunction
import org.bukkit.event.Listener

class EcoEnchantsPlugin : LibreforgePlugin() {
    val targetsYml = TargetsYml(this)
    val rarityYml = RarityYml(this)
    val typesYml = TypesYml(this)
    val vanillaEnchantsYml = VanillaEnchantsYml(this)
    var isLoaded = false
        private set

    init {
        instance = this
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            EcoEnchants
        )
    }

    override fun handleEnable() {
        registerHolderProvider { it.heldEnchantLevels }
        registerPlayerRefreshFunction { it.clearEnchantCache() }
        registerHolderPlaceholderProvider { (holder, _) ->
            when (holder) {
                is EcoEnchantLevel -> listOf(NamedValue("level", holder.level))
                else -> emptyList()
            }
        }
    }

    override fun handleAfterLoad() {
        isLoaded = true
    }

    override fun handleReload() {
        registerVanillaEnchants(this)

        DisplayCache.reload()
        EnchantSorter.reload(this)
        ExtraItemSupport.reload(this)
        EnchantGUI.reload(this)
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            VillagerSupport(this),
            EnchantingTableSupport(this),
            LootSupport(this),
            AnvilSupport(this),
            LoreConversion(this),
            GrindstoneSupport(this)
        )
    }

    override fun loadIntegrationLoaders(): List<IntegrationLoader> {
        return listOf(
            IntegrationLoader("Essentials") { EnchantRegistrations.register(EssentialsIntegration()) },
            IntegrationLoader("CMI") { EnchantRegistrations.register(CMIIntegration()) }
        )
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandEcoEnchants(this),
            CommandEnchantInfo(this)
        )
    }

    override fun createDisplayModule(): DisplayModule? {
        return if (configYml.getBool("display.enabled")) {
            EnchantDisplay(this)
        } else null
    }

    companion object {
        /** Instance of EcoEnchants. */
        @JvmStatic
        lateinit var instance: EcoEnchantsPlugin
            private set
    }
}

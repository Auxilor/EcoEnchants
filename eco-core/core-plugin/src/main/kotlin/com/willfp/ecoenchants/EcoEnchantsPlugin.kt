package com.willfp.ecoenchants

import com.willfp.ecoenchants.commands.ecoEnchantsCommand
import com.willfp.ecoenchants.commands.enchantCommand
import com.willfp.ecoenchants.commands.enchantInfoCommand
import com.willfp.ecoenchants.config.RarityYml
import com.willfp.ecoenchants.config.TargetsYml
import com.willfp.ecoenchants.config.TypesYml
import com.willfp.ecoenchants.config.VanillaEnchantsYml
import com.willfp.ecoenchants.display.DescriptionEnabledPlaceholder
import com.willfp.ecoenchants.display.DisplayCache
import com.willfp.ecoenchants.display.EnchantDisplay
import com.willfp.ecoenchants.display.EnchantSorter
import com.willfp.ecoenchants.dragdrop.EcoEnchantBookDragAndDropHandler
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.EnchantGUI
import com.willfp.ecoenchants.enchant.LoreConversion
import com.willfp.ecoenchants.enchant.VanillaEnchantmentOverridesYml
import com.willfp.ecoenchants.integrations.plugins.CMIIntegration
import com.willfp.ecoenchants.integrations.plugins.EssentialsIntegration
import com.willfp.ecoenchants.libreforge.EffectApplyRandomEnchant
import com.willfp.ecoenchants.libreforge.EffectRemoveThisEnchant
import com.willfp.ecoenchants.mechanics.AnvilRequirementSupport
import com.willfp.ecoenchants.mechanics.EcoEnchantsAnvilHandler
import com.willfp.ecoenchants.mechanics.EnchantingTableSupport
import com.willfp.ecoenchants.mechanics.ExtraItemSupport
import com.willfp.ecoenchants.mechanics.GrindstoneSupport
import com.willfp.ecoenchants.mechanics.LootSupport
import com.willfp.ecoenchants.mechanics.VillagerSupport
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.target.EnchantHolderProvider
import com.willfp.ecoenchants.target.EnchantmentTargets
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.event.Listener
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.command.EcoCommand
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.core.stdlib.NamedValue
import wiki.ecohub.game.item.enchant.CustomEnchantments
import wiki.ecohub.modules.menus.drag.DragAndDropHandlers
import wiki.ecohub.modules.recipe.anvil.AnvilHandlers
import wiki.ecohub.modules.recipe.anvil.AnvilSettings
import wiki.ecohub.provider.ExternalProviderLoader
import wiki.ecohub.provider.metrics.EcoMetricsChart
import wiki.ecohub.rules.effect.Effects
import wiki.ecohub.rules.holder.registerHolderPlaceholderProvider
import wiki.ecohub.rules.holder.registerHolderProvider
import wiki.ecohub.ui.display.DisplayModule

internal lateinit var plugin: EcoEnchantsPlugin
    private set

class EcoEnchantsPlugin : EcoPlugin() {
    val targetsYml = TargetsYml(this)
    val rarityYml = RarityYml(this)
    val typesYml = TypesYml(this)
    val vanillaEnchantsYml = VanillaEnchantsYml(this)
    var isLoaded = false
        private set

    init {
        plugin = this

        CustomEnchantments.setVanillaOverrides(VanillaEnchantmentOverridesYml)
        CustomEnchantments.unfreezeRegistry()
    }

    override fun loadConfigCategories(): List<NativeConfigCategory> {
        return listOf(
            EcoEnchants
        )
    }

    override fun handleEnable() {
        Effects.register(EffectApplyRandomEnchant)
        Effects.register(EffectRemoveThisEnchant)

        registerHolderProvider(EnchantHolderProvider)

        registerHolderPlaceholderProvider<EcoEnchantLevel> { it, _ ->
            listOf(
                NamedValue("level", it.level),
            )
        }

        DescriptionEnabledPlaceholder.register()

        registerAnvilHandler()

        DragAndDropHandlers.register(EcoEnchantBookDragAndDropHandler)
    }

    override fun handleAfterLoad() {
        isLoaded = true

        CustomEnchantments.unfreezeRegistry()
    }

    override fun handleReload() {
        DisplayCache.reload()
        EnchantSorter.reload()
        ExtraItemSupport.reload()
        EnchantGUI.reload()

        registerAnvilHandler()
    }

    override fun handleDisable() {
        DragAndDropHandlers.unregisterAll("ecoenchants")
    }

    private fun registerAnvilHandler() {
        AnvilHandlers.register(
            EcoEnchantsAnvilHandler(),
            AnvilSettings(
                costExponent = configYml.getDouble("anvil.cost-exponent"),
                enchantLimit = configYml.getInt("anvil.enchant-limit"),
                useReworkPenalty = configYml.getBool("anvil.use-rework-penalty"),
                maxRepairCost = configYml.getInt("anvil.max-repair-cost"),
                clampRepairCost = configYml.getBool("anvil.clamp-repair-cost"),
                colorNameAllowed = { it.hasPermission("ecoenchants.anvil.color") }
            )
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            VillagerSupport,
            EnchantingTableSupport,
            LootSupport,
            LoreConversion,
            GrindstoneSupport,
            AnvilRequirementSupport
        )
    }

    override fun getExternalProviderLoaders(): List<ExternalProviderLoader> {
        return listOf(
            ExternalProviderLoader("Essentials") { EssentialsIntegration },
            ExternalProviderLoader("CMI") { CMIIntegration }
        )
    }

    override fun loadPluginCommands(): List<EcoCommand> {
        return listOf(
            ecoEnchantsCommand(),
            enchantInfoCommand(),
            enchantCommand()
        )
    }

    override fun loadDisplayModules(): List<DisplayModule> {
        if (!this.configYml.getBool("display.enabled")) {
            return emptyList()
        }

        return listOf(
            EnchantDisplay
        )
    }

    override fun getCustomCharts() = listOf(
        EcoMetricsChart.SingleLine("total_enchants") { EcoEnchants.values().size },
        EcoMetricsChart.SingleLine("total_rarities") { EnchantmentRarities.values().size },
        EcoMetricsChart.SingleLine("total_targets") { EnchantmentTargets.values().size },
        EcoMetricsChart.SingleLine("total_types") { EnchantmentTypes.values().size },
        EcoMetricsChart.SimplePie("display_enabled") {
            if (configYml.getBool("display.enabled")) "enabled" else "disabled"
        }
    )
}

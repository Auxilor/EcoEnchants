package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.eco.util.NumberUtils
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.ecoenchants.enchants.conflictsWithDeep
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentOffer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.enchantment.PrepareItemEnchantEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min


class EnchantingTableSupport(
    private val plugin: EcoEnchantsPlugin
) : Listener {
    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        ExtraItemSupport.currentlyEnchantingExtraItem.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onEnchant(event: EnchantItemEvent) {
        val player = event.enchanter
        val item = event.item
        val cost = event.expLevelCost
        val toAdd = event.enchantsToAdd

        if (!plugin.configYml.getBool("enchanting-table.enabled")) {
            return
        }

        val isExtraEnchantable = ExtraItemSupport.extraEnchantableItems.any { it.matches(item) }

        if (isExtraEnchantable) {
            val lapis = event.inventory.getItem(1)

            if (player.gameMode != GameMode.CREATIVE) {
                if (lapis == null) {
                    event.isCancelled = true
                    return
                }

                val lapisCost = event.whichButton() + 1

                if (lapis.amount < lapisCost) {
                    event.isCancelled = true
                    return
                }

                lapis.amount -= lapisCost

                event.inventory.setItem(1, lapis)
            }
        }

        if (item.type == Material.BOOK) {
            item.type = Material.ENCHANTED_BOOK
        }

        var multiplier = 0.01
        if (item.type == Material.ENCHANTED_BOOK) {
            multiplier *= plugin.configYml.getDouble("enchanting-table.book-multiplier")
        }

        val enchantments = EcoEnchants.values().shuffled()

        for (enchantment in enchantments) {
            if (!enchantment.isEnchantable) {
                continue
            }

            if (!enchantment.canEnchantItem(item)) {
                continue
            }

            if (!player.hasPermission("ecoenchants.fromtable.${enchantment.id}")) {
                continue
            }

            if (NumberUtils.randFloat(0.0, 1.0) > enchantment.enchantmentRarity.tableChance * multiplier) {
                continue
            }

            if (enchantment.enchantmentRarity.minimumLevel > cost) {
                continue
            }

            if (toAdd.size >= this.plugin.configYml.getInt("enchanting-table.cap")) {
                break
            }

            if (toAdd.size > plugin.configYml.getInt("anvil.enchant-limit").infiniteIfNegative()) {
                break
            }

            if (toAdd.any { (it, _) -> enchantment.conflictsWithDeep(it) }) {
                continue
            }

            if (
                toAdd.keys.filterIsInstance<EcoEnchant>()
                    .count { it.type == enchantment.type } >= enchantment.type.limit
            ) {
                continue
            }

            val maxLevel = enchantment.maxLevel
            val maxObtainableLevel = plugin.configYml.getInt("enchanting-table.maximum-obtainable-level")

            val levelPart1 = cost / maxObtainableLevel.toDouble()
            val levelPart2 = NumberUtils.bias(levelPart1, enchantment.type.highLevelBias)
            val levelPart3 = NumberUtils.triangularDistribution(0.0, 1.0, levelPart2)
            val level = ceil(levelPart3 * maxLevel).coerceIn(1.0..maxLevel.toDouble()).toInt()

            multiplier /= this.plugin.configYml.getDouble("enchanting-table.reduction")

            toAdd[enchantment] = level
        }

        toAdd.forEach(event.enchantsToAdd::putIfAbsent)

        if (toAdd.isEmpty() && isExtraEnchantable) {
            toAdd[Enchantment.DURABILITY] =
                ExtraItemSupport.currentlyEnchantingExtraItem[player.uniqueId]!![event.whichButton()]
            ExtraItemSupport.currentlyEnchantingExtraItem.remove(player.uniqueId)
        }
    }

    @EventHandler
    fun handleExtraItem(event: PrepareItemEnchantEvent) {
        if (!plugin.configYml.getBool("enchanting-table.enabled")) {
            return
        }

        val item = event.item

        if (!ExtraItemSupport.extraEnchantableItems.any { it.matches(item) }) {
            return
        }

        val maxObtainableLevel = plugin.configYml.getInt("enchanting-table.maximum-obtainable-level")

        /*
        I have no clue how this code works, it's transplanted from old EcoEnchants, and I know it works
        perfectly, it's effectively 1:1 with vanilla if memory serves, so I'm not going to rewrite it.
         */

        event.offers.getOrNull(2)?.cost = min(event.offers[2].cost, maxObtainableLevel)

        val bonus = event.enchantmentBonus.coerceIn(1..15)

        val baseLevel = NumberUtils.randInt(1, 8) + floor(bonus / 2.0) + NumberUtils.randInt(0, bonus)

        val levelScale = ceil(maxObtainableLevel / 30.0).toInt()

        val bottomEnchantLevel =
            (ceil((baseLevel / 3).coerceAtLeast(1.0)).toInt() * levelScale).coerceAtMost(maxObtainableLevel)
        val midEnchantLevel = ((baseLevel * 2 / 3).toInt() + 1) * levelScale
        val topEnchantLevel = baseLevel.coerceAtLeast(bonus * 2.0).toInt() * levelScale

        var midUnbreakingLevel = NumberUtils.randInt(1, 3)
        if (midUnbreakingLevel < 2) {
            midUnbreakingLevel = 2
        }
        if (midEnchantLevel < 15) {
            midUnbreakingLevel = 1
        }

        var topUnbreakingLevel = 3
        if (topEnchantLevel < 20) {
            topUnbreakingLevel = 2
        }
        if (topEnchantLevel < 10) {
            topUnbreakingLevel = 1
        }

        val offers = arrayOf(
            EnchantmentOffer(Enchantment.DURABILITY, 1, bottomEnchantLevel),
            EnchantmentOffer(Enchantment.DURABILITY, midUnbreakingLevel, midEnchantLevel),
            EnchantmentOffer(Enchantment.DURABILITY, topUnbreakingLevel, topEnchantLevel)
        )

        for (i in offers.indices) {
            event.offers[i] = offers[i]
        }

        ExtraItemSupport.currentlyEnchantingExtraItem[event.enchanter.uniqueId] = arrayOf(
            event.offers[0].enchantmentLevel,
            event.offers[1].enchantmentLevel,
            event.offers[2].enchantmentLevel
        )
    }
}

object ExtraItemSupport {
    internal val currentlyEnchantingExtraItem = mutableMapOf<UUID, Array<Int>>()

    internal val extraEnchantableItems = mutableListOf<TestableItem>()

    @JvmStatic
    @ConfigUpdater
    fun reload(plugin: EcoEnchantsPlugin) {
        extraEnchantableItems.clear()
        extraEnchantableItems.addAll(plugin.targetsYml.getStrings("extra-enchantable-items").map {
            Items.lookup(it)
        }.filterNot { it is EmptyTestableItem })
    }
}

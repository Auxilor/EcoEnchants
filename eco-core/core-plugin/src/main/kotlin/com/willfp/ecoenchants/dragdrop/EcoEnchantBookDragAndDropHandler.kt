package com.willfp.ecoenchants.dragdrop

import com.willfp.eco.core.dragdrop.DragAndDropHandler
import com.willfp.eco.core.dragdrop.DragAndDropResult
import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.ecoenchants.enchant.wrap
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object EcoEnchantBookDragAndDropHandler : DragAndDropHandler {
    override val id = "ecoenchants:book"

    private fun eligibleEnchants(cursor: ItemStack, current: ItemStack): Map<Enchantment, Int> {
        val meta = cursor.itemMeta as? EnchantmentStorageMeta ?: return emptyMap()
        val storedEnchants = meta.storedEnchants
        if (storedEnchants.isEmpty()) return emptyMap()

        val eligible = mutableMapOf<Enchantment, Int>()

        for ((enchant, level) in storedEnchants) {
            val ecoEnchant = enchant.wrap()
            if (!ecoEnchant.isDragAndDropEnabled()) continue
            if (!ecoEnchant.canEnchantItem(current, eligible.keys)) continue

            val existingLevel = getExistingLevel(current, enchant)
            if (existingLevel != null && level < existingLevel) continue // not an upgrade, skip so book isn't consumed for no effect

            eligible[enchant] = level
        }

        return eligible
    }

    override fun matches(cursor: ItemStack, current: ItemStack): Boolean {
        return eligibleEnchants(cursor, current).isNotEmpty()
    }

    override fun apply(player: Player, cursor: ItemStack, current: ItemStack): DragAndDropResult {
        val eligible = eligibleEnchants(cursor, current)
        if (eligible.isEmpty()) return DragAndDropResult.DENIED

        val prices: List<Triple<Enchantment, Int, Pair<ConfiguredPrice, Double>>> = eligible.map { (enchant, level) ->
            val ecoEnchant = enchant.wrap()
            val price = ecoEnchant.dragAndDropPrice()
            val multiplier = ecoEnchant.dragAndDropPriceMultiplier(level)
            Triple(enchant, level, price to multiplier)
        }

        val canAffordAll = prices.all { (_, _, priceAndMultiplier) ->
            val (price, multiplier) = priceAndMultiplier
            price.canAfford(player, multiplier)
        }
        if (!canAffordAll) return DragAndDropResult.DENIED

        for ((enchant, level, priceAndMultiplier) in prices) {
            val (price, multiplier) = priceAndMultiplier
            price.pay(player, multiplier)

            val existingLevel = getExistingLevel(current, enchant)
            val newLevel = if (existingLevel != null) {
                mergeDragAndDropLevel(existingLevel, level, enchant.maxLevel)
            } else {
                level
            }

            applyEnchant(current, enchant, newLevel)
        }

        return DragAndDropResult.APPLIED
    }

    private fun getExistingLevel(item: ItemStack, enchant: Enchantment): Int? {
        val meta = item.itemMeta ?: return null
        return if (meta is EnchantmentStorageMeta) {
            meta.getStoredEnchantLevel(enchant).takeIf { meta.hasStoredEnchant(enchant) }
        } else {
            meta.getEnchantLevel(enchant).takeIf { meta.hasEnchant(enchant) }
        }
    }

    private fun applyEnchant(item: ItemStack, enchant: Enchantment, level: Int) {
        val meta = item.itemMeta ?: return
        if (meta is EnchantmentStorageMeta) {
            meta.addStoredEnchant(enchant, level, true)
        } else {
            meta.addEnchant(enchant, level, true)
        }
        item.itemMeta = meta
    }
}

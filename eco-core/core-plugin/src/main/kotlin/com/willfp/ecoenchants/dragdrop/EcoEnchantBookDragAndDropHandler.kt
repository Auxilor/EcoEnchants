package com.willfp.ecoenchants.dragdrop

import com.willfp.eco.core.dragdrop.DragAndDropHandler
import com.willfp.eco.core.dragdrop.DragAndDropResult
import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.ecoenchants.enchant.removeEnchantsReplacedBy
import com.willfp.ecoenchants.enchant.wrap
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object EcoEnchantBookDragAndDropHandler : DragAndDropHandler {
    override val id = "ecoenchants:book"

    private fun eligibleEnchants(cursor: ItemStack, current: ItemStack): Map<Enchantment, Int> {
        val meta = cursor.itemMeta as? EnchantmentStorageMeta ?: return emptyMap()
        val pending = meta.storedEnchants.toMutableMap()
        val eligible = mutableMapOf<Enchantment, Int>()

        generateSequence {
            pending.keys.firstOrNull { enchant -> isEligible(enchant, pending.getValue(enchant), current, eligible) }
        }.forEach { enchant ->
            eligible[enchant] = pending.getValue(enchant)
            pending -= enchant
        }

        return eligible
    }

    private fun isEligible(
        enchant: Enchantment,
        level: Int,
        current: ItemStack,
        eligible: Map<Enchantment, Int>
    ): Boolean {
        val ecoEnchant = enchant.wrap()
        val existingLevel = getExistingLevel(current, enchant)
        val isUpgrade = existingLevel == null || level >= existingLevel

        return ecoEnchant.isDragAndDropEnabled() && ecoEnchant.canEnchantItem(current, eligible) && isUpgrade
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

        val added = mutableListOf<Enchantment>()

        for ((enchant, level, priceAndMultiplier) in prices) {
            val (price, multiplier) = priceAndMultiplier
            price.pay(player, multiplier)

            val existingLevel = getExistingLevel(current, enchant)
            val newLevel = if (existingLevel != null) {
                mergeDragAndDropLevel(existingLevel, level, enchant.maxLevel)
            } else {
                added += enchant
                level
            }

            applyEnchant(current, enchant, newLevel)
        }

        current.removeEnchantsReplacedBy(added)

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

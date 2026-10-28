package com.willfp.ecoenchants.enchant.impl.hardcoded

import com.willfp.eco.util.DurabilityUtils
import com.willfp.ecoenchants.enchant.impl.HardcodedEcoEnchant
import com.willfp.ecoenchants.runOwned
import com.willfp.ecoenchants.target.EnchantFinder.getItemsWithEnchantActive
import com.willfp.ecoenchants.target.EnchantFinder.hasEnchantActive
import com.willfp.libreforge.slot.impl.SlotTypeArmor
import com.willfp.libreforge.slot.impl.SlotTypeHands
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object EnchantmentRepairing : HardcodedEcoEnchant(
    "repairing"
) {
    private const val STORAGE_SLOTS = 36

    override fun onRegister() {
        if (!plugin.isLoaded) {
            return
        }

        val frequency = config.getInt("frequency").toLong()

        plugin.scheduler.global().runTimer(frequency, frequency) {
            val notWhileHolding = config.getBool("not-while-holding")

            for (player in Bukkit.getOnlinePlayers()) {
                // Each player's inventory belongs to their own region on Folia.
                player.runOwned {
                    handleRepairing(player, notWhileHolding)
                }
            }
        }
    }

    private fun handleRepairing(player: Player, notWhileHolding: Boolean) {
        if (!player.isOnline || !player.hasEnchantActive(this)) {
            return
        }

        val repairPerLevel = config.getIntFromExpression("repair-per-level", player)

        val items = player.getItemsWithEnchantActive(this)

        if (!notWhileHolding) {
            for ((item, level) in items) {
                DurabilityUtils.repairItem(item, level * repairPerLevel)
            }
            return
        }

        repairNotHeldOrWorn(player, items, repairPerLevel)
    }

    private fun repairNotHeldOrWorn(player: Player, items: List<Pair<ItemStack, Int>>, repairPerLevel: Int) {
        val pending = items.map { (item, level) -> PendingRepair(item.clone(), item, level) }.toMutableList()
        val heldOrWorn = (SlotTypeHands.getItems(player) + SlotTypeArmor.getItems(player))
            .map { it.clone() }
            .toMutableList()

        for (index in 0 until STORAGE_SLOTS) {
            val item = player.inventory.getItem(index)
            val match = pending.indexOfFirst { it.snapshot == item }

            if (index != player.inventory.heldItemSlot && item != null && match >= 0) {
                DurabilityUtils.repairItem(item, pending.removeAt(match).level * repairPerLevel)
            }
        }

        for (repair in pending) {
            val held = heldOrWorn.indexOfFirst { it == repair.snapshot }

            if (held >= 0) {
                heldOrWorn.removeAt(held)
            } else {
                DurabilityUtils.repairItem(repair.item, repair.level * repairPerLevel)
            }
        }
    }

    private data class PendingRepair(
        val snapshot: ItemStack,
        val item: ItemStack,
        val level: Int
    )
}

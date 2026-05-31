package com.willfp.ecoenchants.enchant.impl.hardcoded

import com.willfp.eco.util.DurabilityUtils
import com.willfp.ecoenchants.enchant.impl.HardcodedEcoEnchant
import com.willfp.ecoenchants.target.EnchantFinder.getItemsWithEnchantActive
import com.willfp.libreforge.slot.impl.SlotTypeArmor
import com.willfp.libreforge.slot.impl.SlotTypeHands
import org.bukkit.Bukkit

object EnchantmentRepairing : HardcodedEcoEnchant(
    "repairing"
) {
    override fun onRegister() {
        val frequency = config.getInt("frequency").toLong()

        plugin.scheduler.runTimer(frequency, frequency) {
            handleRepairing()
        }
    }

    private fun handleRepairing() {
        val notWhileHolding = config.getBool("not-while-holding")

        for (player in Bukkit.getOnlinePlayers()) {
            val activeItems = player.getItemsWithEnchantActive(this)
            if (activeItems.isEmpty()) {
                continue
            }

            val repairPerLevel = config.getIntFromExpression("repair-per-level", player)
            val excludedItems = if (notWhileHolding) {
                SlotTypeHands.getItems(player).toSet() + SlotTypeArmor.getItems(player)
            } else {
                emptySet()
            }

            for ((item, level) in activeItems) {
                if (item in excludedItems) {
                    continue
                }

                DurabilityUtils.repairItem(item, level * repairPerLevel)
            }
        }
    }
}

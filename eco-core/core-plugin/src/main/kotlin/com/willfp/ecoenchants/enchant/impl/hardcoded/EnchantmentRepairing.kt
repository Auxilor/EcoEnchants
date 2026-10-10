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

object EnchantmentRepairing : HardcodedEcoEnchant(
    "repairing"
) {
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

        for ((item, level) in player.getItemsWithEnchantActive(this)) {
            if (notWhileHolding) {
                val isHolding = item in SlotTypeHands.getItems(player)
                val isEquipped = item in SlotTypeArmor.getItems(player)

                if (isHolding || isEquipped) {
                    continue
                }
            }

            DurabilityUtils.repairItem(item, level * repairPerLevel)
        }
    }
}

package com.willfp.ecoenchants.enchant.impl.hardcoded

import com.willfp.eco.util.DurabilityUtils
import com.willfp.ecoenchants.enchant.impl.HardcodedEcoEnchant
import com.willfp.ecoenchants.target.EnchantFinder.getItemsWithEnchantActive
import com.willfp.libreforge.slot.impl.SlotTypeArmor
import com.willfp.libreforge.slot.impl.SlotTypeHands
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.function.Consumer

object EnchantmentRepairing : HardcodedEcoEnchant(
    "repairing"
) {
    override fun onRegister() {
        val frequency = config.getInt("frequency").toLong()

        plugin.scheduler.runTimer(frequency, frequency) {
            for (player in Bukkit.getOnlinePlayers()) {
                player.scheduler.run(plugin, Consumer {
                    handleRepairing(player)
                }, null)
            }
        }
    }

    private fun handleRepairing(player: Player) {
        val notWhileHolding = config.getBool("not-while-holding")

        val activeItems = player.getItemsWithEnchantActive(this)
        if (activeItems.isEmpty()) {
            return
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

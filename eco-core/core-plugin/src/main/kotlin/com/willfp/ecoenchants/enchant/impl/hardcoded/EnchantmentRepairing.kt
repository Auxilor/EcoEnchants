package com.willfp.ecoenchants.enchant.impl.hardcoded

import com.willfp.eco.util.DurabilityUtils
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.impl.HardcodedEcoEnchant
import com.willfp.ecoenchants.target.EnchantFinder.getItemsWithEnchantActive
import com.willfp.ecoenchants.target.EnchantFinder.hasEnchantActive
import com.willfp.libreforge.slot.impl.SlotTypeHands
import org.bukkit.Bukkit

class EnchantmentRepairing(
    plugin: EcoEnchantsPlugin
) : HardcodedEcoEnchant(
    "repairing",
    plugin
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
            if (player.hasEnchantActive(this)) {
                val repairPerLevel = config.getIntFromExpression("repair-per-level", player)

                for ((item, level) in player.getItemsWithEnchantActive(this)) {
                    val isHolding = item in SlotTypeHands.getItems(player)

                    if (notWhileHolding && isHolding) {
                        continue
                    }

                    DurabilityUtils.repairItem(item, level * repairPerLevel)
                }
            }
        }
    }
}

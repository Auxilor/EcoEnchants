package com.willfp.ecoenchants.enchants.impl

import com.willfp.eco.util.DurabilityUtils
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.target.EnchantLookup.getActiveEnchantLevelInSlot
import com.willfp.ecoenchants.target.EnchantLookup.hasEnchantActive
import com.willfp.libreforge.slot.impl.SlotTypeHands
import org.bukkit.Bukkit

class EnchantmentRepairing(
    plugin: EcoEnchantsPlugin
) : EcoEnchant(
    "repairing",
    plugin,
    force = false
) {
    override fun onInit() {
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

                for ((slot, item) in player.inventory.withIndex()) {
                    if (item == null) {
                        continue
                    }

                    if (notWhileHolding && slot in SlotTypeHands.getItemSlots(player)) {
                        continue
                    }

                    val level = player.getActiveEnchantLevelInSlot(this, slot)

                    if (level == 0) {
                        continue
                    }

                    DurabilityUtils.repairItem(item, level * repairPerLevel)
                }
            }
        }
    }
}

package com.willfp.ecoenchants.enchants.impl

import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.target.EnchantLookup.hasEnchantActive
import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class EnchantmentReplenish(
    plugin: EcoEnchantsPlugin
) : EcoEnchant(
    "replenish",
    plugin,
    force = false
) {
    override fun onInit() {
        this.registerListener(ReplenishHandler(this, plugin))
    }

    private class ReplenishHandler(
        private val enchant: EcoEnchant,
        private val plugin: EcoEnchantsPlugin
    ) : Listener {
        @EventHandler(
            ignoreCancelled = true
        )
        fun handle(event: BlockBreakEvent) {
            val player = event.player

            if (!player.hasEnchantActive(enchant)) {
                return
            }

            val block = event.block
            val type = block.type

            if (type in arrayOf(
                    Material.GLOW_BERRIES,
                    Material.SWEET_BERRY_BUSH,
                    Material.CACTUS,
                    Material.BAMBOO,
                    Material.CHORUS_FLOWER,
                    Material.SUGAR_CANE
                )
            ) {
                return
            }

            val data = block.blockData

            if (data !is Ageable) {
                return
            }

            if (data.age != data.maximumAge) {
                event.isDropItems = false
                event.expToDrop = 0
            }

            data.age = 0

            plugin.scheduler.run {
                block.type = type
                block.blockData = data
            }
        }
    }
}

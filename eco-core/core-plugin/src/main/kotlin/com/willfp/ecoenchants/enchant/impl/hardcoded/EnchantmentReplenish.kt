package com.willfp.ecoenchants.enchant.impl.hardcoded

import com.willfp.eco.core.EcoPlugin
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.impl.HardcodedEcoEnchant
import com.willfp.ecoenchants.target.EnchantFinder.hasEnchantActive
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Ageable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class EnchantmentReplenish(
    plugin: EcoEnchantsPlugin
) : HardcodedEcoEnchant(
    "replenish",
    plugin
) {
    private var handler = ReplenishHandler(this, plugin)

    override fun onRegister() {
        plugin.eventManager.registerListener(handler)
    }

    override fun onRemove() {
        plugin.eventManager.unregisterListener(handler)
    }

    private class ReplenishHandler(
        private val enchant: EcoEnchant,
        private val plugin: EcoPlugin
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

            if (enchant.config.getBool("consume-seeds")) {
                val item = ItemStack(
                    when (type) {
                        Material.WHEAT -> Material.WHEAT_SEEDS
                        Material.POTATOES -> Material.POTATO
                        Material.CARROTS -> Material.CARROT
                        Material.BEETROOTS -> Material.BEETROOT_SEEDS
                        Material.COCOA -> Material.COCOA_BEANS
                        else -> type
                    }
                )

                val hasSeeds = player.inventory.removeItem(item).isEmpty()

                if (!hasSeeds) {
                    return
                }
            }

            if (data.age != data.maximumAge) {
                if (enchant.config.getBool("only-fully-grown")) {
                    return
                }

                event.isDropItems = false
                event.expToDrop = 0
            }

            data.age = 0

            plugin.scheduler.run {
                block.type = type
                block.blockData = data

                // Improves compatibility with other plugins.
                Bukkit.getPluginManager().callEvent(
                    BlockPlaceEvent(
                        block,
                        block.state,
                        block.getRelative(BlockFace.DOWN),
                        player.inventory.itemInMainHand,
                        player,
                        true,
                        EquipmentSlot.HAND
                    )
                )
            }
        }
    }
}

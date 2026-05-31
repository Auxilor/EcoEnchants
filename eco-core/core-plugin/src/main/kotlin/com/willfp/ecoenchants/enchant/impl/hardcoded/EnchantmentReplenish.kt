package com.willfp.ecoenchants.enchant.impl.hardcoded

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
import java.util.EnumSet

object EnchantmentReplenish : HardcodedEcoEnchant(
    "replenish"
) {
    private var handler = ReplenishHandler(this)
    private val ignoredCropTypes = EnumSet.of(
        Material.GLOW_BERRIES,
        Material.SWEET_BERRY_BUSH,
        Material.CACTUS,
        Material.BAMBOO,
        Material.CHORUS_FLOWER,
        Material.SUGAR_CANE
    )
    private val seedTypes = mapOf(
        Material.WHEAT to Material.WHEAT_SEEDS,
        Material.POTATOES to Material.POTATO,
        Material.CARROTS to Material.CARROT,
        Material.BEETROOTS to Material.BEETROOT_SEEDS,
        Material.COCOA to Material.COCOA_BEANS,
        Material.NETHER_WART to Material.NETHER_WART,
        Material.TORCHFLOWER_CROP to Material.TORCHFLOWER_SEEDS,
        Material.PITCHER_CROP to Material.PITCHER_POD,
        Material.MELON_STEM to Material.MELON_SEEDS,
        Material.PUMPKIN_STEM to Material.PUMPKIN_SEEDS
    )

    override fun onRegister() {
        plugin.eventManager.registerListener(handler)
    }

    override fun onRemove() {
        plugin.eventManager.unregisterListener(handler)
    }

    private class ReplenishHandler(
        private val enchant: EcoEnchant
    ) : Listener {
        @EventHandler(
            ignoreCancelled = true
        )
        fun handle(event: BlockBreakEvent) {
            val block = event.block
            val type = block.type

            if (type in ignoredCropTypes) {
                return
            }

            val data = block.blockData

            if (data !is Ageable) {
                return
            }

            val player = event.player

            if (!player.hasEnchantActive(enchant)) {
                return
            }

            val wasFullyGrown = data.age == data.maximumAge
            if (!wasFullyGrown && enchant.config.getBool("only-fully-grown")) {
                return
            }

            if (enchant.config.getBool("consume-seeds")) {
                val seedType = seedTypes[type] ?: type
                if (!seedType.isItem) {
                    return
                }

                val item = ItemStack(seedType)

                val hasSeeds = player.inventory.removeItem(item).isEmpty()

                if (!hasSeeds) {
                    return
                }
            }

            if (!wasFullyGrown) {
                event.isDropItems = false
                event.expToDrop = 0
            }

            data.age = 0
            val itemInHand = player.inventory.itemInMainHand.clone()

            plugin.scheduler.run {
                if (!block.type.isAir) {
                    return@run
                }

                val replacedState = block.state
                block.type = type
                block.blockData = data

                // Improves compatibility with other plugins.
                @Suppress("UnstableApiUsage")
                val placeEvent = BlockPlaceEvent(
                    block,
                    replacedState,
                    block.getRelative(BlockFace.DOWN),
                    itemInHand,
                    player,
                    true,
                    EquipmentSlot.HAND
                )
                Bukkit.getPluginManager().callEvent(placeEvent)

                if (placeEvent.isCancelled || !placeEvent.canBuild()) {
                    replacedState.update(true, false)
                }
            }
        }
    }
}

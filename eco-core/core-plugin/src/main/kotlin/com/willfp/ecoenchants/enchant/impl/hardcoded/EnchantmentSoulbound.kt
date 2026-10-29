package com.willfp.ecoenchants.enchant.impl.hardcoded

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.data.keys.PersistentDataKey
import com.willfp.eco.core.data.keys.PersistentDataKeyType
import com.willfp.eco.core.data.profile
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.items.Items
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.impl.HardcodedEcoEnchant
import com.willfp.ecoenchants.target.EnchantFinder.getItemsWithEnchantActive
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object EnchantmentSoulbound : HardcodedEcoEnchant(
    "soulbound"
) {
    private val handler = SoulboundHandler(this)

    override fun onRegister() {
        if (!plugin.isLoaded) {
            return
        }

        plugin.eventManager.registerListener(handler)
    }

    override fun onRemove() {
        plugin.eventManager.unregisterListener(handler)
    }

    private class SoulboundHandler(
        private val enchant: EcoEnchant
    ) : Listener {
        private val savedSoulboundItems = PersistentDataKey(
            plugin.namespacedKeyFactory.create("soulbound_items"),
            PersistentDataKeyType.STRING_LIST,
            emptyList()
        )

        private val soulboundKey = plugin.namespacedKeyFactory.create("soulbound")

        @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
        )
        fun handle(event: PlayerDeathEvent) {
            if (event.keepInventory) {
                return
            }

            val player = event.entity
            val items = player.getItemsWithEnchantActive(enchant)
                .map { it.first }
                .filter { event.drops.remove(it) }

            if (items.isEmpty()) {
                return
            }

            // Use native paper method
            if (Prerequisite.HAS_PAPER.isMet) {
                keepOnPaper(event, items)
            } else {
                saveForRespawn(player, items)
            }
        }

        private fun keepOnPaper(event: PlayerDeathEvent, items: List<ItemStack>) {
            val modifiedItems = if (enchant.config.getBool("single-use")) {
                items.map {
                    val meta = it.itemMeta ?: return@map it
                    meta.removeEnchant(enchant.enchantment)
                    it.itemMeta = meta
                    it
                }
            } else {
                items
            }

            event.itemsToKeep += modifiedItems
        }

        private fun saveForRespawn(player: Player, items: List<ItemStack>) {
            for (item in items) {
                item.fast().persistentDataContainer.set(soulboundKey, PersistentDataType.INTEGER, 1)

                if (enchant.config.getBool("single-use")) {
                    val meta = item.itemMeta ?: continue
                    meta.removeEnchant(enchant.enchantment)
                    item.itemMeta = meta
                }
            }

            player.profile.write(savedSoulboundItems, items.map { Items.toSNBT(it) })
        }

        @EventHandler(
            ignoreCancelled = true
        )
        fun onJoin(event: PlayerJoinEvent) {
            if (!event.player.isDead) {
                giveItems(event.player)
            }
        }

        @EventHandler(
            ignoreCancelled = true
        )
        fun onJoin(event: PlayerRespawnEvent) {
            giveItems(event.player)
        }

        private fun giveItems(player: Player) {
            val saved = player.profile.read(savedSoulboundItems)

            if (saved.isEmpty()) {
                return
            }

            player.profile.write(savedSoulboundItems, emptyList())

            plugin.scheduler.on(player).onRetired {
                player.profile.write(savedSoulboundItems, player.profile.read(savedSoulboundItems) + saved)
            }.run {
                DropQueue(player)
                    .addItems(saved.map { Items.fromSNBT(it) })
                    .forceTelekinesis()
                    .push()
            }
        }

        @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
        )
        fun preventDroppingSoulboundItems(event: PlayerDeathEvent) {
            event.drops.removeIf {
                it.fast().persistentDataContainer.has(soulboundKey, PersistentDataType.INTEGER)
                        && it.itemMeta?.hasEnchant(enchant.enchantment) == true
            }
        }
    }
}

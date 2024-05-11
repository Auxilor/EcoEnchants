package com.willfp.ecoenchants.enchant.impl.hardcoded

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.data.keys.PersistentDataKey
import com.willfp.eco.core.data.keys.PersistentDataKeyType
import com.willfp.eco.core.data.profile
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.items.Items
import com.willfp.ecoenchants.EcoEnchantsPlugin
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
import org.bukkit.persistence.PersistentDataType

class EnchantmentSoulbound(
    plugin: EcoEnchantsPlugin
) : HardcodedEcoEnchant(
    "soulbound",
    plugin
) {
    private val handler = SoulboundHandler(plugin, this)

    override fun onRegister() {
        plugin.eventManager.registerListener(handler)
    }

    override fun onRemove() {
        plugin.eventManager.unregisterListener(handler)
    }

    private class SoulboundHandler(
        private val plugin: EcoPlugin,
        private val enchant: EcoEnchant
    ) : Listener {
        private val savedSoulboundItems = PersistentDataKey(
            plugin.namespacedKeyFactory.create("soulbound_items"),
            PersistentDataKeyType.STRING_LIST,
            emptyList()
        )

        private val soulboundKey = plugin.namespacedKeyFactory.create("soulbound")

        @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
        )
        fun handle(event: PlayerDeathEvent) {
            if (event.keepInventory) {
                return
            }

            val player = event.entity
            val items = player.getItemsWithEnchantActive(enchant).keys

            if (items.isEmpty()) {
                return
            }

            event.drops.removeAll(items)

            // Use native paper method
            if (Prerequisite.HAS_PAPER.isMet) {
                val modifiedItems = if (enchant.config.getBool("single-use")) {
                    items.map {
                        val meta = it.itemMeta
                        meta.removeEnchant(enchant.enchantment)
                        it.itemMeta = meta
                        it
                    }
                } else {
                    items
                }

                event.itemsToKeep += modifiedItems
                return
            }

            for (item in items) {
                item.fast().persistentDataContainer.set(soulboundKey, PersistentDataType.INTEGER, 1)

                if (enchant.config.getBool("single-use")) {
                    val meta = item.itemMeta
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
            giveItems(event.player)
        }

        @EventHandler(
            ignoreCancelled = true
        )
        fun onJoin(event: PlayerRespawnEvent) {
            giveItems(event.player)
        }

        private fun giveItems(player: Player) {
            val itemStrings = player.profile.read(savedSoulboundItems)

            if (itemStrings.isEmpty()) {
                return
            }

            val items = itemStrings.map { Items.fromSNBT(it) }

            plugin.scheduler.run {
                DropQueue(player)
                    .addItems(items)
                    .forceTelekinesis()
                    .push()
            }

            player.profile.write(savedSoulboundItems, emptyList())
        }

        @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
        )
        fun preventDroppingSoulboundItems(event: PlayerDeathEvent) {
            event.drops.removeIf {
                it.fast().persistentDataContainer.has(soulboundKey, PersistentDataType.INTEGER)
                        && it.itemMeta.hasEnchant(enchant.enchantment)
            }
        }
    }
}

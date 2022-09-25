package com.willfp.ecoenchants.enchants

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.eco.core.items.builder.EnchantedBookBuilder
import com.willfp.eco.util.formatEco
import com.willfp.ecoenchants.display.getFormattedName
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

object EnchantInfo {
    private val menus: Cache<EcoEnchant, Menu> = Caffeine.newBuilder()
        .build()

    fun open(player: Player, plugin: EcoPlugin, enchant: EcoEnchant) {
        menus.get(enchant) {
            val level = if (plugin.configYml.getBool("enchantinfo.item.show-max-level")) {
                enchant.maxLevel
            } else {
                1
            }

            menu(plugin.configYml.getInt("enchantinfo.rows")) {
                setTitle(enchant.getFormattedName(0))

                setSlot(
                    plugin.configYml.getInt("enchantinfo.item.row"),
                    plugin.configYml.getInt("enchantinfo.item.column"),
                    slot(
                        EnchantedBookBuilder()
                            .addStoredEnchantment(enchant, level)
                            .writeMetaKey(
                                plugin.namespacedKeyFactory.create("force-describe"),
                                PersistentDataType.INTEGER,
                                1
                            )
                            .addLoreLines {
                                plugin.configYml.getStrings("enchantinfo.item.lore")
                                    .map {
                                        it.replace("%max_level%", enchant.maxLevel.toString())
                                            .replace("%rarity%", enchant.enchantmentRarity.displayName)
                                            .replace(
                                                "%targets%",
                                                enchant.targets.joinToString(", ") { target -> target.displayName }
                                            )
                                            .replace(
                                                "%conflicts%",
                                                enchant.conflicts.joinToString(", ") { conflict ->
                                                    conflict.wrap().getFormattedName(0)
                                                }.ifEmpty { plugin.langYml.getFormattedString("no-conflicts") }
                                            )
                                    }
                                    .formatEco()
                            }
                            .build()
                    )
                )

                setMask(
                    FillerMask(
                        MaskItems.fromItemNames(plugin.configYml.getStrings("enchantinfo.mask.items")),
                        *plugin.configYml.getStrings("enchantinfo.mask.pattern").toTypedArray()
                    )
                )
            }
        }.open(player)
    }
}

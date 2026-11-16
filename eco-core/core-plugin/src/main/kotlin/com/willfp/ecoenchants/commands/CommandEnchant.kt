package com.willfp.ecoenchants.commands

import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.wrap
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.util.StringUtil
import wiki.ecohub.core.command.command
import wiki.ecohub.game.entity.player.PlayerUtils
import wiki.ecohub.game.entity.runOwned
import wiki.ecohub.ui.text.StringUtils

internal fun EcoEnchantsPlugin.enchantCommand() = command(name = "enchant", permission = "ecoenchants.command.enchant") {
    executes {
        val player = sender as? Player
            ?: argument(0, missingKey = "invalid-player", invalidKey = "invalid-player") { Bukkit.getPlayer(it) }
        val enchantArgs = if (sender is Player) args else args.drop(1)

        val enchant = enchantArgs.getOrNull(0)?.lowercase()
            ?.let {
                @Suppress("DEPRECATION")
                Enchantment.getByKey(NamespacedKey.minecraft(it))
            }
            ?: fail("invalid-enchantment")

        val level = enchantArgs.getOrNull(1)?.toIntOrNull() ?: 1

        // The console runs off the player's region on Folia.
        player.runOwned {
            val item = player.inventory.itemInMainHand

            val meta = item.itemMeta

            if (level > 0) {
                if (meta is EnchantmentStorageMeta) {
                    meta.addStoredEnchant(enchant, level, true)
                }
                meta.addEnchant(enchant, level, true)

                sender.sendMessage(
                    plugin.langYml.getMessage("added-enchant", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                        .replace("%enchant%", enchant.wrap().getFormattedName(0))
                        .replace("%player%", PlayerUtils.getSavedDisplayName(player))
                )
            } else {
                if (meta is EnchantmentStorageMeta) {
                    meta.removeStoredEnchant(enchant)
                }
                meta.removeEnchant(enchant)

                sender.sendMessage(
                    plugin.langYml.getMessage("removed-enchant", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                        .replace("%enchant%", enchant.wrap().getFormattedName(0))
                        .replace("%player%", PlayerUtils.getSavedDisplayName(player))
                )
            }

            item.itemMeta = meta
        }
    }

    completes {
        val completions = mutableListOf<String>()

        val enchantArgs = if (sender is Player) args else args.drop(1)

        if (enchantArgs.size == 1) {
            StringUtil.copyPartialMatches(
                enchantArgs[0],
                @Suppress("DEPRECATION")
                Enchantment.values().map { it.key.key },
                completions
            )
        }

        if (enchantArgs.size == 2) {
            @Suppress("DEPRECATION")
            val enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantArgs[0].lowercase()))

            val levels = if (enchant != null) {
                val maxLevel = enchant.maxLevel
                (0..maxLevel).toList()
            } else {
                (0..5).toList()
            }

            StringUtil.copyPartialMatches(
                enchantArgs[1],
                levels.map { it.toString() },
                completions
            )
        }

        completions
    }
}

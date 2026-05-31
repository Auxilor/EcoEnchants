package com.willfp.ecoenchants.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.savedDisplayName
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.util.StringUtil

object CommandEnchant : PluginCommand(
    plugin,
    "enchant",
    "ecoenchants.command.enchant",
    false
) {
    private var enchantmentCompletions: List<String> = emptyList()
    private var levelCompletionsByEnchant = emptyMap<String, List<String>>()
    private val defaultLevelCompletions = (0..5).map { it.toString() }

    internal fun reload() {
        @Suppress("DEPRECATION")
        val enchantments = Enchantment.values()

        enchantmentCompletions = enchantments.map { it.key.key }
        levelCompletionsByEnchant = enchantments.associate {
            it.key.key to (0..it.maxLevel).map { level -> level.toString() }
        }
    }

    override fun onExecute(sender: CommandSender, rawArgs: List<String>) {
        var args = rawArgs
        var player = sender as? Player

        if (sender !is Player) {
            player = notifyPlayerRequired(args.getOrNull(0), "invalid-player")
            args = rawArgs.subList(1, rawArgs.size)
        }

        player!! // Unbelievable jank

        val enchant = notifyNull(
            @Suppress("DEPRECATION")
            args.getOrNull(0)?.lowercase()?.let { Enchantment.getByKey(NamespacedKey.minecraft(it)) },
            "invalid-enchantment"
        )

        val level = args.getOrNull(1)?.toIntOrNull() ?: 1

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
                    .replace("%player%", player.savedDisplayName)
            )
        } else {
            if (meta is EnchantmentStorageMeta) {
                meta.removeStoredEnchant(enchant)
            }
            meta.removeEnchant(enchant)

            sender.sendMessage(
                plugin.langYml.getMessage("removed-enchant", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                    .replace("%enchant%", enchant.wrap().getFormattedName(0))
                    .replace("%player%", player.savedDisplayName)
            )
        }

        item.itemMeta = meta
    }

    override fun tabComplete(sender: CommandSender, rawArgs: List<String>): List<String> {
        if (enchantmentCompletions.isEmpty()) {
            reload()
        }

        val completions = mutableListOf<String>()

        var args = rawArgs

        if (sender !is Player) {
            args = rawArgs.subList(1, rawArgs.size)
        }

        if (args.size == 1) {
            StringUtil.copyPartialMatches(
                args[0],
                enchantmentCompletions,
                completions
            )
        }

        if (args.size == 2) {
            @Suppress("DEPRECATION")
            val enchant = Enchantment.getByKey(NamespacedKey.minecraft(args[0].lowercase()))

            val levels = if (enchant != null) {
                levelCompletionsByEnchant[enchant.key.key] ?: defaultLevelCompletions
            } else {
                defaultLevelCompletions
            }

            StringUtil.copyPartialMatches(
                args[1],
                levels,
                completions
            )
        }

        return completions
    }
}

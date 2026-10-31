package com.willfp.ecoenchants.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.items.builder.EnchantedBookBuilder
import com.willfp.eco.util.StringUtils
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.runOwned
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.util.StringUtil

private const val DEFAULT_TAB_COMPLETE_MAX_LEVEL = 5

private val amountCompletions = listOf("1", "2", "4", "8", "16", "32", "64")

object CommandGiveBook : PluginCommand(
    plugin,
    "givebook",
    "ecoenchants.command.givebook",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val player = notifyNull(
            Bukkit.getPlayer(notifyNull(args.getOrNull(0), "requires-player")),
            "invalid-player"
        )

        val enchantments = parseEnchantments(notifyNull(args.getOrNull(1), "missing-enchant"))

        notifyFalse(enchantments.isNotEmpty(), "missing-enchant")

        val amount = notifyFalse(
            args.getOrNull(2)?.let { it.toIntOrNull() ?: 0 } ?: 1,
            "invalid-amount"
        ) { it >= 1 }

        val item = EnchantedBookBuilder().apply {
            enchantments.forEach { (enchantment, level) -> addStoredEnchantment(enchantment, level) }
        }.build()

        player.runOwned {
            DropQueue(player)
                .addItems(List(amount) { item.clone() })
                .forceTelekinesis()
                .push()
        }

        sender.sendMessage(
            plugin.langYml.getMessage("gave-book", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace(oldValue = "%player%", newValue = player.name)
                .replace(oldValue = "%amount%", newValue = amount.toString())
                .replace(
                    oldValue = "%enchantments%",
                    newValue = enchantments.entries.joinToString(StringUtils.format("&f, ")) { (enchantment, level) ->
                        enchantment.wrap().getFormattedName(level)
                    }
                )
        )
    }

    private fun parseEnchantments(argument: String): Map<Enchantment, Int> {
        val enchantments = mutableMapOf<Enchantment, Int>()

        for (entry in argument.split(",").filter { it.isNotBlank() }) {
            val enchantment = notifyNull(getEnchantment(entry.substringBefore(":")), "invalid-enchantment")

            val level = notifyFalse(
                if (entry.contains(":")) entry.substringAfter(":").toIntOrNull() ?: 0 else 1,
                "invalid-level"
            ) { it >= 1 }

            enchantments[enchantment] = level
        }

        return enchantments
    }

    @Suppress("DEPRECATION")
    private fun getEnchantment(id: String): Enchantment? =
        Enchantment.getByKey(NamespacedKey.minecraft(id.lowercase()))

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        when (args.lastIndex) {
            0 -> {
                StringUtil.copyPartialMatches(
                    args[0],
                    Bukkit.getOnlinePlayers().map { it.name },
                    completions
                )
            }

            1 -> {
                StringUtil.copyPartialMatches(args[1], enchantmentCompletions(args[1]), completions)
            }

            2 -> {
                StringUtil.copyPartialMatches(args[2], amountCompletions, completions)
            }
        }

        return completions
    }

    private fun enchantmentCompletions(argument: String): List<String> {
        val previous = argument.substringBeforeLast(delimiter = ",", missingDelimiterValue = "")
        val prefix = if (previous.isEmpty()) "" else "$previous,"
        val current = argument.substringAfterLast(",")

        if (!current.contains(":")) {
            @Suppress("DEPRECATION")
            return Enchantment.values().map { "$prefix${it.key.key}" }
        }

        val id = current.substringBefore(":")
        val maxLevel = getEnchantment(id)?.run { wrap().maximumLevel } ?: DEFAULT_TAB_COMPLETE_MAX_LEVEL

        return (1..maxLevel).map { "$prefix$id:$it" }
    }
}

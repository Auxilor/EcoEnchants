package com.willfp.ecoenchants.commands

import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.wrap
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.util.StringUtil
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.core.command.CommandContext
import wiki.ecohub.game.drop.DropQueue
import wiki.ecohub.game.entity.runOwned
import wiki.ecohub.game.item.builder.EnchantedBookBuilder
import wiki.ecohub.ui.text.StringUtils

private const val DEFAULT_TAB_COMPLETE_MAX_LEVEL = 5

private val amountCompletions = listOf("1", "2", "4", "8", "16", "32", "64")

internal fun CommandBuilder<CommandSender>.giveBookCommand() = subcommand(name = "givebook", permission = "ecoenchants.command.givebook") {
    executes {
        val player = argument(0, missingKey = "requires-player", invalidKey = "invalid-player") { Bukkit.getPlayer(it) }

        val enchantments = parseEnchantments(argument(1, "missing-enchant"))

        if (enchantments.isEmpty()) {
            fail("missing-enchant")
        }

        val amount = (args.getOrNull(2)?.let { it.toIntOrNull() ?: 0 } ?: 1)
            .takeIf { it >= 1 }
            ?: fail("invalid-amount")

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

    completes {
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

        completions
    }
}

private fun CommandContext<CommandSender>.parseEnchantments(argument: String): Map<Enchantment, Int> {
    val enchantments = mutableMapOf<Enchantment, Int>()

    for (entry in argument.split(",").filter { it.isNotBlank() }) {
        val enchantment = getEnchantment(entry.substringBefore(":")) ?: fail("invalid-enchantment")

        val level = (if (entry.contains(":")) entry.substringAfter(":").toIntOrNull() ?: 0 else 1)
            .takeIf { it >= 1 }
            ?: fail("invalid-level")

        enchantments[enchantment] = level
    }

    return enchantments
}

@Suppress("DEPRECATION")
private fun getEnchantment(id: String): Enchantment? =
    Enchantment.getByKey(NamespacedKey.minecraft(id.lowercase()))

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

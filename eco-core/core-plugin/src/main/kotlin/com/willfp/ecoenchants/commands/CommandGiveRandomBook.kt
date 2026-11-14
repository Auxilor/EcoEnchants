package com.willfp.ecoenchants.commands

import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchantLike
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.Bukkit
import org.bukkit.Registry
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.game.drop.DropQueue
import wiki.ecohub.game.entity.runOwned
import wiki.ecohub.game.item.builder.EnchantedBookBuilder
import wiki.ecohub.math.NumberUtils
import wiki.ecohub.ui.text.StringUtils

private const val INCLUDE_VANILLA_INDEX = 4

internal fun CommandBuilder<CommandSender>.giveRandomBookCommand() = subcommand(name = "giverandombook", permission = "ecoenchants.command.giverandombook") {
    executes {
        val playerName = args.getOrNull(0)

        if (playerName == null) {
            sender.sendMessage(plugin.langYml.getMessage("requires-player"))
            return@executes
        }

        val player = Bukkit.getPlayer(playerName)

        if (player == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return@executes
        }

        val filterName = args.getOrNull(1)

        val filter = if (filterName != null) {
            EnchantmentTypes[filterName] ?: EnchantmentRarities[filterName]
        } else null

        val minLevel = args.getOrNull(2)?.toIntOrNull() ?: 1
        val maxLevel = args.getOrNull(3)?.toIntOrNull() ?: Int.MAX_VALUE

        if (minLevel > maxLevel) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-levels"))
            return@executes
        }

        val enchantment = getEnchantmentPool(args)
            .filter {
                when (filter) {
                    is EnchantmentRarity -> it.enchantmentRarity == filter
                    is EnchantmentType -> it.type == filter
                    else -> true
                } && it.maximumLevel >= minLevel
            }
            .randomOrNull()
            ?: run {
                sender.sendMessage(plugin.langYml.getMessage("no-enchantments-found"))
                return@executes
            }

        val level = NumberUtils.randInt(minLevel, maxLevel.coerceAtMost(enchantment.maximumLevel))

        val item = EnchantedBookBuilder()
            .addStoredEnchantment(enchantment.enchantment, level)
            .build()

        // The console runs off the player's region on Folia.
        player.runOwned {
            DropQueue(player)
                .addItem(item)
                .forceTelekinesis()
                .push()
        }

        sender.sendMessage(
            plugin.langYml.getMessage("gave-random-book", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace("%player%", player.name)
                .replace("%enchantment%", enchantment.getFormattedName(level))
        )
    }

    completes {
        // OfTeN wrote this - it's cursed, and I am *not* going to try refactor this.
        when (args.size) {
            1 -> Bukkit.getOnlinePlayers().map { it.name }
            2 -> listOf("all") + EnchantmentRarities.values().map { it.id } + EnchantmentTypes.values().map { it.id }
            3 -> (1..10).map { it.toString() }
            4 -> {
                val startLevel = args[2].toIntOrNull() ?: 1
                val endLevel = startLevel + 10
                (startLevel..endLevel).map { it.toString() }
            }

            INCLUDE_VANILLA_INDEX + 1 -> listOf("true", "false")

            else -> emptyList()
        }
    }
}

private fun getEnchantmentPool(args: List<String>): Collection<EcoEnchantLike> =
    if (args.getOrNull(INCLUDE_VANILLA_INDEX)?.toBoolean() == true) {
        Registry.ENCHANTMENT.map { it.wrap() }
    } else {
        EcoEnchants.values()
    }

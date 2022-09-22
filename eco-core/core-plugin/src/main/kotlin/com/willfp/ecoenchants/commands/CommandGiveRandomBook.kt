package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.items.builder.EnchantedBookBuilder
import com.willfp.eco.util.NumberUtils
import com.willfp.ecoenchants.enchants.EcoEnchants
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.util.StringUtil
import java.util.*
import java.util.stream.Collectors.toList

class CommandGiveRandomBook(plugin: EcoPlugin) : Subcommand(
    plugin,
    "giverandombook",
    "ecoenchants.command.giverandombook",
    false
) {

    override fun onExecute(
        sender: CommandSender,
        args: List<String>
    ) {

        if (args.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("requires-player"))
            return
        }

        val player = Bukkit.getServer().getPlayer(args[0])

        val rarity: com.willfp.ecoenchants.rarity.EnchantmentRarity? =
            if (args.size >= 2) com.willfp.ecoenchants.rarity.EnchantmentRarities.getByID(
                args[1].lowercase(Locale.getDefault())
            ) else null

        val type: com.willfp.ecoenchants.type.EnchantmentType? =
            if (rarity == null && args.size >= 2) com.willfp.ecoenchants.type.EnchantmentTypes.getByID(
                args[1].lowercase(Locale.getDefault())
            ) else null

        if (player == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return
        }

        val allowed = Arrays.stream(Enchantment.values()).filter { enchantment: Enchantment ->
            if (enchantment is com.willfp.ecoenchants.enchants.EcoEnchant) {
                if (rarity != null) {
                    return@filter enchantment.rarity == rarity
                }
                if (type != null) {
                    return@filter enchantment.type == type
                }
                return@filter true
            }
            false
        }.collect(toList())

        val enchantment = allowed[NumberUtils.randInt(0, allowed.size - 1)]
        val level = NumberUtils.randInt(1, enchantment.maxLevel)
        val itemStack = EnchantedBookBuilder()
            .addStoredEnchantment(enchantment, level)
            .build()

        //Add the item to the player's inventory if there is space, otherwise drop it at their feet
        if (player.inventory.firstEmpty() == -1) {
            player.world.dropItem(player.location, itemStack)
        } else {
            player.inventory.addItem(itemStack)
        }

        var message = plugin.langYml.getMessage("gave-random-book")
        message = message.replace(
            "%enchantment%",
            enchantment.displayName(level).toString() + "§r"
        )
        sender.sendMessage(message)

        var message2 = plugin.langYml.getMessage("received-random-book")
        message2 = message2.replace(
            "%enchantment%",
            enchantment.displayName(level).toString() + "§r"
        )
        player.sendMessage(message2)
    }

    override fun tabComplete( sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        val playerNames = EcoEnchants.values().mapNotNull { ChatColor.stripColor(it.displayName) }

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return playerNames
        }

        if (args.size == 1) {
            StringUtil.copyPartialMatches( args[0], playerNames, completions )
        }

        if (args.size == 2) {
            StringUtil.copyPartialMatches(
                java.lang.String.join(
                    " ",
                    args[1]
                ), RARITY_NAMES, completions
            )
            StringUtil.copyPartialMatches(
                java.lang.String.join(
                    " ",
                    args[1]
                ), TYPE_NAMES, completions
            )
        }

        completions.sort()
        return completions
    }

    companion object {
        /**
         * Cache the rarities for tab completion
         */
        private var RARITY_NAMES: List<String> = com.willfp.ecoenchants.rarity.EnchantmentRarities.values().map { it.id }

        /**
         * Cache the type names for tab completion
         */
        private var TYPE_NAMES: List<String> = com.willfp.ecoenchants.type.EnchantmentTypes.values().map { it.id }


        /**
         * Called on /reload.
         */
        @ConfigUpdater
        fun reload() {
            //Clear and re-cache rarities
            RARITY_NAMES = com.willfp.ecoenchants.rarity.EnchantmentRarities.values().map { it.id }

            //Clear and re-cache types
            TYPE_NAMES = com.willfp.ecoenchants.type.EnchantmentTypes.values().map { it.id }
        }
    }
}
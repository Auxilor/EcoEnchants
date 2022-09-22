package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.items.builder.EnchantedBookBuilder
import com.willfp.eco.util.NumberUtils
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
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

        val filteredEnchantments = Arrays.stream(Enchantment.values()).filter { enchantment: Enchantment ->
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

        if (filteredEnchantments.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("no-enchantments"))
            return
        }

        //Handle the case where there is only one enchantment to avoid rand exception
        val randEnchant: Enchantment = if (filteredEnchantments.size == 1) {
            filteredEnchantments[0]
        } else {
            filteredEnchantments[NumberUtils.randInt(0, filteredEnchantments.size - 1)]
        }

        val level = NumberUtils.randInt(randEnchant.startLevel, randEnchant.maxLevel)
        val itemStack = EnchantedBookBuilder()
            .addStoredEnchantment(randEnchant, level)
            .build()

        //Add the item to the player's inventory if there is space, otherwise drop it at their feet
        if (player.inventory.firstEmpty() == -1) {
            player.world.dropItem(player.location, itemStack)
        } else {
            player.inventory.addItem(itemStack)
        }

        var message = plugin.langYml.getMessage("gave-random-book")
        //Player replacement doesn't work for some reason with %player%
        message = message.replace(
            "%targetplayer%",
            player.name + "§r"
        )
        message = message.replace(
            "%enchantment%",
            PlainTextComponentSerializer.plainText().serialize(randEnchant.displayName(level)) + "§r"
        )
        sender.sendMessage(message)

        var message2 = plugin.langYml.getMessage("received-random-book")
        message2 = message2.replace(
            "%enchantment%",
            PlainTextComponentSerializer.plainText().serialize(randEnchant.displayName(level)) + "§r"
        )
        player.sendMessage(message2)
    }

    override fun tabComplete( sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        //Create a list of online players for auto-completion
        val playerNames = Bukkit.getServer().onlinePlayers.stream().map(Player::getName).collect(toList())

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
package com.willfp.ecoenchants.commands

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.core.platform.NamespacedKeyUtils
import wiki.ecohub.data.PersistentDataKey
import wiki.ecohub.data.PersistentDataKeyType
import wiki.ecohub.game.entity.player.profile

private val descriptionsKey = PersistentDataKey(
    NamespacedKeyUtils.create("ecoenchants", "descriptions_enabled"),
    PersistentDataKeyType.BOOLEAN,
    true
)

val Player.seesEnchantmentDescriptions: Boolean
    get() = this.profile.read(descriptionsKey)

internal fun CommandBuilder<CommandSender>.toggleDescriptionsCommand() =
    playerSubcommand(name = "toggledescriptions", permission = "ecoenchants.command.toggledescriptions") {
        executes {
            if (!plugin.configYml.getBool("display.descriptions.enabled")) {
                sender.sendMessage(plugin.langYml.getMessage("descriptions-disabled"))
                return@executes
            }

            var currentStatus = sender.profile.read(descriptionsKey)
            currentStatus = !currentStatus
            sender.profile.write(descriptionsKey, currentStatus)
            if (currentStatus) {
                sender.sendMessage(plugin.langYml.getMessage("enabled-descriptions"))
            } else {
                sender.sendMessage(plugin.langYml.getMessage("disabled-descriptions"))
            }
        }
    }

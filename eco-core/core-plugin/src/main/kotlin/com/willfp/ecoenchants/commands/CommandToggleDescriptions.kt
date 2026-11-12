package com.willfp.ecoenchants.commands

import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.ecoenchants.plugin
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import wiki.ecohub.core.platform.NamespacedKeyUtils
import wiki.ecohub.data.PersistentDataKey
import wiki.ecohub.data.PersistentDataKeyType
import wiki.ecohub.game.entity.player.profile

object CommandToggleDescriptions : Subcommand(
    plugin,
    "toggledescriptions",
    "ecoenchants.command.toggledescriptions",
    true
) {

    override fun onExecute(
        player: CommandSender,
        args: List<String>
    ) {
        player as Player

        if (!plugin.configYml.getBool("display.descriptions.enabled")) {
            player.sendMessage(plugin.langYml.getMessage("descriptions-disabled"))
            return
        }

        var currentStatus = player.profile.read(descriptionsKey)
        currentStatus = !currentStatus
        player.profile.write(descriptionsKey, currentStatus)
        if (currentStatus) {
            player.sendMessage(plugin.langYml.getMessage("enabled-descriptions"))
        } else {
            player.sendMessage(plugin.langYml.getMessage("disabled-descriptions"))
        }
    }

    private val descriptionsKey = PersistentDataKey(
        NamespacedKeyUtils.create("ecoenchants", "descriptions_enabled"),
        PersistentDataKeyType.BOOLEAN,
        true
    )

    val Player.seesEnchantmentDescriptions: Boolean
        get() = this.profile.read(descriptionsKey)
}

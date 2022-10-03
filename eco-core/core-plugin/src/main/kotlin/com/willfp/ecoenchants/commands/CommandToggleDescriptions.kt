package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.core.data.keys.PersistentDataKey
import com.willfp.eco.core.data.keys.PersistentDataKeyType
import com.willfp.eco.core.data.profile
import com.willfp.eco.util.NamespacedKeyUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandToggleDescriptions(plugin: EcoPlugin) : Subcommand(
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

    companion object {
        private val descriptionsKey = PersistentDataKey(
            NamespacedKeyUtils.create("ecoenchants", "descriptions_enabled"),
            PersistentDataKeyType.BOOLEAN,
            true
        )

        val Player.seesEnchantmentDescriptions: Boolean
            get() = this.profile.read(descriptionsKey)
    }
}

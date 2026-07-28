package com.willfp.ecoenchants.display

import com.willfp.eco.core.placeholder.PlayerPlaceholder
import com.willfp.ecoenchants.commands.CommandToggleDescriptions.seesEnchantmentDescriptions
import com.willfp.ecoenchants.plugin

object DescriptionEnabledPlaceholder {
    fun register() {
        PlayerPlaceholder(plugin, "descriptions_enabled") { player ->
            player.seesEnchantmentDescriptions.toString()
        }.register()
    }
}

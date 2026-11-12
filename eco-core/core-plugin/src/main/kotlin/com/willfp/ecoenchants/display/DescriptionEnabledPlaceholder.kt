package com.willfp.ecoenchants.display

import com.willfp.ecoenchants.commands.seesEnchantmentDescriptions
import com.willfp.ecoenchants.plugin
import wiki.ecohub.ui.placeholder.type.PlayerPlaceholder

object DescriptionEnabledPlaceholder {
    fun register() {
        PlayerPlaceholder(plugin, "descriptions_enabled") { player ->
            player.seesEnchantmentDescriptions.toString()
        }.register()
    }
}

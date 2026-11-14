package com.willfp.ecoenchants.commands

import com.willfp.ecoenchants.EcoEnchantsPlugin
import wiki.ecohub.core.command.command

internal fun EcoEnchantsPlugin.ecoEnchantsCommand() = command(name = "ecoenchants", permission = "ecoenchants.command.ecoenchants") {
    reloadCommand()
    toggleDescriptionsCommand()
    giveRandomBookCommand()
    giveBookCommand()
    guiCommand()
}

package com.willfp.ecoenchants.display

import org.bukkit.inventory.ItemFlag
import wiki.ecohub.core.plugin.startup.Prerequisite
import wiki.ecohub.game.item.FastItemStack

private val storedEnchantsFlag: ItemFlag
    get() = if (Prerequisite.HAS_PAPER.isMet) ItemFlag.HIDE_STORED_ENCHANTS else ItemFlag.HIDE_ADDITIONAL_TOOLTIP

internal fun FastItemStack.hideStoredEnchants() = addItemFlags(storedEnchantsFlag)

internal fun FastItemStack.showStoredEnchants() = removeItemFlags(storedEnchantsFlag)

internal val FastItemStack.areStoredEnchantsHidden: Boolean
    get() = hasItemFlag(storedEnchantsFlag)

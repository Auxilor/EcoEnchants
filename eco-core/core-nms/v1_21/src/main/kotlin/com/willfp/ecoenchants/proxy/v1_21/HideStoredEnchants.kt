package com.willfp.ecoenchants.proxy.v1_21

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.ecoenchants.display.HideStoredEnchantsProxy
import org.bukkit.inventory.ItemFlag
import javax.print.attribute.PrintRequestAttribute

class HideStoredEnchants: HideStoredEnchantsProxy {
    override fun hideStoredEnchants(fis: FastItemStack) {
        if (Prerequisite.HAS_PAPER.isMet) {
            fis.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
        } else {
            fis.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
        }
    }

    override fun areStoredEnchantsHidden(fis: FastItemStack): Boolean {
        return if (Prerequisite.HAS_PAPER.isMet) {
            fis.hasItemFlag(ItemFlag.HIDE_STORED_ENCHANTS)
        } else {
            fis.hasItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
        }
    }

    override fun showStoredEnchants(fis: FastItemStack) {
        if (Prerequisite.HAS_PAPER.isMet) {
            fis.removeItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
        } else {
            fis.removeItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
        }
    }
}

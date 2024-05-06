package com.willfp.ecoenchants.proxy.v1_19_R3

import com.willfp.eco.core.fast.FastItemStack
import com.willfp.ecoenchants.display.HideStoredEnchantsProxy
import org.bukkit.inventory.ItemFlag

class HideStoredEnchants: HideStoredEnchantsProxy {
    override fun hideStoredEnchants(fis: FastItemStack) {
        fis.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
    }

    override fun areStoredEnchantsHidden(fis: FastItemStack): Boolean {
        return fis.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
    }

    override fun showStoredEnchants(fis: FastItemStack) {
        fis.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
    }
}

package com.willfp.ecoenchants.proxy.v1_21

import com.willfp.eco.core.fast.FastItemStack
import com.willfp.ecoenchants.display.HideStoredEnchantsProxy
import org.bukkit.inventory.ItemFlag

class HideStoredEnchants: HideStoredEnchantsProxy {
    override fun hideStoredEnchants(fis: FastItemStack) {
        fis.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
    }

    override fun areStoredEnchantsHidden(fis: FastItemStack): Boolean {
        return fis.hasItemFlag(ItemFlag.HIDE_STORED_ENCHANTS)
    }

    override fun showStoredEnchants(fis: FastItemStack) {
        fis.removeItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
    }
}

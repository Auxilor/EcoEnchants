package com.willfp.ecoenchants.proxy.v1_21_4

import com.willfp.ecoenchants.mechanics.OpenInventoryProxy
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

class OpenInventory : OpenInventoryProxy {
    override fun getOpenInventory(player: Player): Any {
        return (player as CraftPlayer).handle.containerMenu
    }
}

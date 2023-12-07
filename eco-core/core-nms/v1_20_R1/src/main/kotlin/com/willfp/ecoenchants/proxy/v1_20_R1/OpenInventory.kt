package com.willfp.ecoenchants.proxy.v1_20_R1

import com.willfp.ecoenchants.mechanics.OpenInventoryProxy
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.entity.Player

class OpenInventory : OpenInventoryProxy {
    override fun getOpenInventory(player: Player): Any {
        return (player as CraftPlayer).handle.bR
    }
}

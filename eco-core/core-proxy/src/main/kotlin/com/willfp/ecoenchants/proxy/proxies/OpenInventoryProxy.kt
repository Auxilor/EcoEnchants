package com.willfp.ecoenchants.proxy.proxies

import org.bukkit.entity.Player

interface OpenInventoryProxy {
    fun getOpenInventory(player: Player): Any
}

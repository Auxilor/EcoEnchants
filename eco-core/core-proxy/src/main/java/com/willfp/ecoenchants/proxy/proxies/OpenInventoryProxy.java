package com.willfp.ecoenchants.proxy.proxies;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface OpenInventoryProxy {
    /**
     * Get the NMS inventory container for a player's inventory view.
     *
     * @param player The player to query.
     * @return The NMS inventory container.
     */
    Object getOpenInventory(@NotNull Player player);
}

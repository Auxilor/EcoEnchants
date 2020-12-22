package com.willfp.eco.core.proxy.proxies;

import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.entity.Player;

/**
 * Utility class to get the NMS implementation of a players' currently open inventory
 */
public interface OpenInventoryProxy extends AbstractProxy {
    Object getOpenInventory(Player player);
}
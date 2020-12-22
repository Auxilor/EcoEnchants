package com.willfp.eco.core.proxy.proxies;

import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class to get and set the anvil rework penalty of an item
 */
public interface RepairCostProxy extends AbstractProxy {
    ItemStack setRepairCost(ItemStack itemStack, int cost);
    int getRepairCost(ItemStack itemStack);
}

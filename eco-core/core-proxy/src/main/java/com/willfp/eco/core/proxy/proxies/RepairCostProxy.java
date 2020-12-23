package com.willfp.eco.core.proxy.proxies;

import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface RepairCostProxy extends AbstractProxy {
    /**
     * Set the rework penalty of an item.
     *
     * @param itemStack The item to query.
     * @param cost      The rework penalty to set.
     * @return The item, with the rework penalty applied.
     */
    ItemStack setRepairCost(@NotNull ItemStack itemStack,
                            int cost);

    /**
     * Get the rework penalty of an item.
     *
     * @param itemStack The item to query.
     * @return The rework penalty found on the item.
     */
    int getRepairCost(@NotNull ItemStack itemStack);
}

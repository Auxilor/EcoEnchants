package com.willfp.eco.core.proxy.proxies;


import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface TridentStackProxy extends AbstractProxy {
    
    /**
     * Get a trident's ItemStack.
     *
     * @param trident The trident to query.
     * @return The trident's ItemStack.
     */
    ItemStack getTridentStack(@NotNull Trident trident);
}

package com.willfp.eco.core.proxy.proxies;


import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class to get the {@link ItemStack} of a given {@link Trident}
 */
public interface TridentStackProxy extends AbstractProxy {
    ItemStack getTridentStack(Trident trident);
}
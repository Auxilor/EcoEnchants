package com.willfp.eco.core.proxy.proxies;

import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface FastGetEnchantsProxy extends AbstractProxy {
    /**
     * Get all enchantments on an {@link ItemStack}.
     *
     * @param itemStack The item to query.
     * @return A map of all enchantments, where the value represents the level present.
     */
    Map<Enchantment, Integer> getEnchantmentsOnItem(@NotNull ItemStack itemStack);

    /**
     * Get the level of a specified enchantment on an item.
     * @param itemStack The item to query.
     * @param enchantment The enchantment to query.
     * @return The level found, or 0 if not present.
     */
    int getLevelOnItem(@NotNull ItemStack itemStack,
                       @NotNull Enchantment enchantment);
}

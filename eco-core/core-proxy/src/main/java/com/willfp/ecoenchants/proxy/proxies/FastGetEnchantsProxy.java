package com.willfp.ecoenchants.proxy.proxies;

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
    default Map<Enchantment, Integer> getEnchantmentsOnItem(@NotNull ItemStack itemStack) {
        return getEnchantmentsOnItem(itemStack, false);
    }

    /**
     * Get all enchantments on an {@link ItemStack}.
     *
     * @param itemStack The item to query.
     * @param checkStored Check stored enchantments in the enchanted book if true.
     * @return A map of all enchantments, where the value represents the level present.
     */
    Map<Enchantment, Integer> getEnchantmentsOnItem(@NotNull ItemStack itemStack, boolean checkStored);

    /**
     * Get the level of a specified enchantment on an item.
     *
     * @param itemStack   The item to query.
     * @param enchantment The enchantment to query.
     * @return The level found, or 0 if not present.
     */
    default int getLevelOnItem(@NotNull ItemStack itemStack,
                                @NotNull Enchantment enchantment) {
        return getLevelOnItem(itemStack, enchantment, false);
    }

    /**
     * Get the level of a specified enchantment on an item.
     *
     * @param itemStack   The item to query.
     * @param enchantment The enchantment to query.
     * @param checkStored Check stored enchantments in the enchanted book if true.
     * @return The level found, or 0 if not present.
     */
    int getLevelOnItem(@NotNull ItemStack itemStack,
                       @NotNull Enchantment enchantment,
                       boolean checkStored);
}

package com.willfp.eco.core.proxy.proxies;

import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Utility class to break a block as if the player had done it manually
 */
public interface FastGetEnchantsProxy extends AbstractProxy {
    Map<Enchantment, Integer> getEnchantmentsOnItem(ItemStack itemStack);
    int getLevelOnItem(ItemStack itemStack, Enchantment enchantment);
}
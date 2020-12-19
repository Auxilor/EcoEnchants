package com.willfp.ecoenchants.nms.api;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface FastGetEnchantsWrapper {
    Map<Enchantment, Integer> getEnchantmentsOnItem(ItemStack itemStack);
}

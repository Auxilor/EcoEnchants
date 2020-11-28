package com.willfp.ecoenchants.display.sorting;

import org.bukkit.enchantments.Enchantment;

import java.util.List;

public interface EnchantmentSorter {
    void sortEnchantments(final List<Enchantment> toSort);
}

package com.willfp.ecoenchants.display.options.sorting;

import com.willfp.ecoenchants.display.EnchantmentCache;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public class AlphabeticSorter implements EnchantmentSorter {
    @Override
    public void sortEnchantments(final List<Enchantment> toSort) {
        toSort.sort(((enchantment1, enchantment2) -> EnchantmentCache.getEntry(enchantment1).getRawName().compareToIgnoreCase(EnchantmentCache.getEntry(enchantment2).getRawName())));
    }
}

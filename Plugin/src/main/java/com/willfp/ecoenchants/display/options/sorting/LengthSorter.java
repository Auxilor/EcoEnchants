package com.willfp.ecoenchants.display.options.sorting;

import com.willfp.ecoenchants.display.EnchantmentCache;
import org.bukkit.enchantments.Enchantment;

import java.util.Comparator;
import java.util.List;

public class LengthSorter implements EnchantmentSorter {
    @Override
    public void sortEnchantments(final List<Enchantment> toSort) {
        toSort.sort(Comparator.comparingInt(enchantment -> EnchantmentCache.getEntry(enchantment).getRawName().length()));
    }
}

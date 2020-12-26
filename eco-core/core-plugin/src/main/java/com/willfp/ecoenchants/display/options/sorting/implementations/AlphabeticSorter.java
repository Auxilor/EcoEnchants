package com.willfp.ecoenchants.display.options.sorting.implementations;

import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlphabeticSorter implements EnchantmentSorter {
    @Override
    public void sortEnchantments(final @NotNull List<Enchantment> toSort) {
        toSort.sort(((enchantment1, enchantment2) -> EnchantmentCache.getEntry(enchantment1).getRawName().compareToIgnoreCase(EnchantmentCache.getEntry(enchantment2).getRawName())));
    }

    @Override
    public SortParameters[] getParameters() {
        return new SortParameters[0];
    }
}

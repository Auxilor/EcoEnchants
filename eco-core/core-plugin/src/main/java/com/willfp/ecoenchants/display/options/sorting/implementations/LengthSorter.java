package com.willfp.ecoenchants.display.options.sorting.implementations;

import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class LengthSorter implements EnchantmentSorter {
    @Override
    public void sortEnchantments(final @NotNull List<Enchantment> toSort) {
        toSort.sort(Comparator.comparingInt(enchantment -> EnchantmentCache.getEntry(enchantment).getRawName().length()));
    }

    @Override
    public SortParameters[] getParameters() {
        return new SortParameters[]{SortParameters.LENGTH};
    }
}

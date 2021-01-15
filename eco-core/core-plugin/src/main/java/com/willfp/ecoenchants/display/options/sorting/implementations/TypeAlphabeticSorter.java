package com.willfp.ecoenchants.display.options.sorting.implementations;

import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TypeAlphabeticSorter implements EnchantmentSorter {
    @Override
    public void sortEnchantments(@NotNull final List<Enchantment> toSort) {
        if (EnchantDisplay.OPTIONS.getSortedRarities().isEmpty() || EnchantDisplay.OPTIONS.getSortedTypes().isEmpty()) {
            EnchantDisplay.update();
        }

        List<Enchantment> sorted = new ArrayList<>();
        EnchantDisplay.OPTIONS.getSortedTypes().forEach(enchantmentType -> {
            List<Enchantment> typeEnchants = new ArrayList<>();
            for (Enchantment enchantment : toSort) {
                if (EnchantmentCache.getEntry(enchantment).getType().equals(enchantmentType)) {
                    typeEnchants.add(enchantment);
                }
            }

            typeEnchants.sort((enchantment1, enchantment2) -> EnchantmentCache.getEntry(enchantment1).getRawName().compareToIgnoreCase(EnchantmentCache.getEntry(enchantment2).getRawName()));
            sorted.addAll(typeEnchants);
        });

        toSort.clear();
        toSort.addAll(sorted);
    }

    @Override
    public SortParameters[] getParameters() {
        return new SortParameters[]{SortParameters.TYPE};
    }
}

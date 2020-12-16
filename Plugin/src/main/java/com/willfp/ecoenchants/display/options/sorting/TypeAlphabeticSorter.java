package com.willfp.ecoenchants.display.options.sorting;

import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.EnchantmentCache;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeAlphabeticSorter implements EnchantmentSorter {
    @Override
    public void sortEnchantments(final List<Enchantment> toSort) {
        List<Enchantment> sorted = new ArrayList<>();
        EnchantDisplay.OPTIONS.getSortedTypes().forEach(enchantmentType -> {
            List<Enchantment> typeEnchants = toSort.stream()
                    .filter(enchantment -> EnchantmentCache.getEntry(enchantment).getType().equals(enchantmentType))
                    .sorted((enchantment1, enchantment2) -> EnchantmentCache.getEntry(enchantment1).getRawName().compareToIgnoreCase(EnchantmentCache.getEntry(enchantment2).getRawName()))
                    .collect(Collectors.toList());
            sorted.addAll(typeEnchants);
        });

        toSort.clear();
        toSort.addAll(sorted);
    }
}

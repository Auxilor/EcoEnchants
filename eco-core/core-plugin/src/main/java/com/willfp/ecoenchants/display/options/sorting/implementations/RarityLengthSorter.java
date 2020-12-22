package com.willfp.ecoenchants.display.options.sorting.implementations;

import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RarityLengthSorter implements EnchantmentSorter {
    @Override
    public void sortEnchantments(final List<Enchantment> toSort) {
        List<Enchantment> sorted = new ArrayList<>();
        EnchantDisplay.OPTIONS.getSortedRarities().forEach(enchantmentRarity -> {
            List<Enchantment> rarityEnchants = toSort.stream()
                    .filter(enchantment -> EnchantmentCache.getEntry(enchantment).getRarity().equals(enchantmentRarity))
                    .sorted(Comparator.comparingInt(enchantment -> EnchantmentCache.getEntry(enchantment).getRawName().length()))
                    .collect(Collectors.toList());
            sorted.addAll(rarityEnchants);
        });

        toSort.clear();
        toSort.addAll(sorted);
    }

    @Override
    public SortParameters[] getParameters() {
        return new SortParameters[]{SortParameters.RARITY, SortParameters.LENGTH};
    }
}

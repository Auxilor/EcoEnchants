package com.willfp.ecoenchants.display.options.sorting.implementations;

import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RarityTypeLengthSorter implements EnchantmentSorter {
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

            typeEnchants.sort(Comparator.comparingInt(enchantment -> EnchantmentCache.getEntry(enchantment).getRawName().length()));

            EnchantDisplay.OPTIONS.getSortedRarities().forEach(enchantmentRarity -> {
                List<Enchantment> rarityEnchants = new ArrayList<>();
                for (Enchantment enchantment : typeEnchants) {
                    if (EnchantmentCache.getEntry(enchantment).getRarity().equals(enchantmentRarity)) {
                        rarityEnchants.add(enchantment);
                    }
                }
                rarityEnchants.sort(Comparator.comparingInt(enchantment -> EnchantmentCache.getEntry(enchantment).getRawName().length()));
            });
        });

        toSort.clear();
        toSort.addAll(sorted);
    }

    @Override
    public SortParameters[] getParameters() {
        return new SortParameters[]{SortParameters.RARITY, SortParameters.TYPE, SortParameters.LENGTH};
    }
}

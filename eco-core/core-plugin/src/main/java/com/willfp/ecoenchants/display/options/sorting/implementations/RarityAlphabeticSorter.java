package com.willfp.ecoenchants.display.options.sorting.implementations;

import com.willfp.eco.core.PluginDependent;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RarityAlphabeticSorter extends PluginDependent<EcoEnchantsPlugin> implements EnchantmentSorter {
    /**
     * Instantiate sorter.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public RarityAlphabeticSorter(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void sortEnchantments(@NotNull final List<Enchantment> toSort) {
        if (this.getPlugin().getDisplayModule().getOptions().getSortedRarities().isEmpty()
                || this.getPlugin().getDisplayModule().getOptions().getSortedTypes().isEmpty()) {
            this.getPlugin().getDisplayModule().update();
        }

        List<Enchantment> sorted = new ArrayList<>();

        this.getPlugin().getDisplayModule().getOptions().getSortedRarities().forEach(enchantmentRarity -> {
            List<Enchantment> rarityEnchants = new ArrayList<>();
            for (Enchantment enchantment : toSort) {
                if (EnchantmentCache.getEntry(enchantment).getRarity().equals(enchantmentRarity)) {
                    rarityEnchants.add(enchantment);
                }
            }
            rarityEnchants.sort((enchantment1, enchantment2) -> EnchantmentCache.getEntry(enchantment1).getRawName().compareToIgnoreCase(EnchantmentCache.getEntry(enchantment2).getRawName()));
            sorted.addAll(rarityEnchants);
        });

        toSort.clear();
        toSort.addAll(sorted);
    }

    @Override
    public SortParameters[] getParameters() {
        return new SortParameters[]{SortParameters.RARITY};
    }
}

package com.willfp.ecoenchants.display.options.sorting.implementations;

import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TypeLengthSorter extends PluginDependent implements EnchantmentSorter {
    /**
     * Instantiate sorter.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public TypeLengthSorter(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void sortEnchantments(@NotNull final List<Enchantment> toSort) {
        if (((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().getSortedRarities().isEmpty()
                || ((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().getSortedTypes().isEmpty()) {
            ((EnchantDisplay) this.getPlugin().getDisplayModule()).update();
        }

        List<Enchantment> sorted = new ArrayList<>();
        ((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().getSortedTypes().forEach(enchantmentType -> {
            List<Enchantment> typeEnchants = new ArrayList<>();
            for (Enchantment enchantment : toSort) {
                if (EnchantmentCache.getEntry(enchantment).getType().equals(enchantmentType)) {
                    typeEnchants.add(enchantment);
                }
            }

            sorted.addAll(typeEnchants);
        });

        toSort.clear();
        toSort.addAll(sorted);
    }

    @Override
    public SortParameters[] getParameters() {
        return new SortParameters[]{SortParameters.TYPE, SortParameters.LENGTH};
    }
}

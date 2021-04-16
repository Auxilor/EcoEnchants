package com.willfp.ecoenchants.display.options.sorting;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EnchantmentSorter {
    /**
     * Sort list of enchantments.
     * <p>
     * All implementations must treat enchantments as final or effectively final.
     *
     * @param toSort The enchantments to sort.
     */
    void sortEnchantments(@NotNull List<Enchantment> toSort);

    /**
     * Get the parameters that the sorter fulfills.
     *
     * @return Array of all parameters.
     */
    SortParameters[] getParameters();
}

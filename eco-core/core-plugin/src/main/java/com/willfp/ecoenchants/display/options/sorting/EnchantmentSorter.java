package com.willfp.ecoenchants.display.options.sorting;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EnchantmentSorter {
    void sortEnchantments(@NotNull List<Enchantment> toSort);
    SortParameters[] getParameters();
}

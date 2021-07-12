package com.willfp.ecoenchants.enchantments.support.merging.grindstone;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@UtilityClass
public class GrindstoneMerge {

    /**
     * Merge items in a grindstone.
     *
     * @param top    The item at the top of the GUI.
     * @param bottom The item at the bottom of the GUI.
     * @return All enchantments for the output item to have.
     */
    public static Map<Enchantment, Integer> doMerge(@Nullable final ItemStack top,
                                                    @Nullable final ItemStack bottom) {
        Map<Enchantment, Integer> bottomEnchants = new HashMap<>();
        Map<Enchantment, Integer> topEnchants = new HashMap<>();

        Map<Enchantment, Integer> toKeep = new HashMap<>();

        if (top != null) {
            if (top.getItemMeta() instanceof EnchantmentStorageMeta) {
                topEnchants = new HashMap<>(((EnchantmentStorageMeta) top.getItemMeta()).getStoredEnchants());
            } else {
                topEnchants = new HashMap<>(top.getEnchantments());
            }
        }

        if (bottom != null) {
            if (bottom.getItemMeta() instanceof EnchantmentStorageMeta) {
                bottomEnchants = new HashMap<>(((EnchantmentStorageMeta) bottom.getItemMeta()).getStoredEnchants());
            } else {
                bottomEnchants = new HashMap<>(bottom.getEnchantments());
            }
        }

        bottomEnchants.forEach(((enchantment, integer) -> {
            if (enchantment instanceof EcoEnchant ecoEnchant) {
                if (!ecoEnchant.isGrindstoneable()) {
                    toKeep.putIfAbsent(enchantment, integer);
                }
            } else {
                if (enchantment.isCursed()) {
                    toKeep.putIfAbsent(enchantment, integer);
                }
            }
        }));
        topEnchants.forEach(((enchantment, integer) -> {
            if (enchantment instanceof EcoEnchant ecoEnchant) {
                if (!ecoEnchant.isGrindstoneable()) {
                    toKeep.putIfAbsent(enchantment, integer);
                }
            } else {
                if (enchantment.isCursed()) {
                    toKeep.putIfAbsent(enchantment, integer);
                }
            }
        }));

        return toKeep;
    }
}

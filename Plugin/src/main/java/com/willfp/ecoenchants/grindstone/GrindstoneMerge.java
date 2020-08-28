package com.willfp.ecoenchants.grindstone;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.HashMap;
import java.util.Map;

public class GrindstoneMerge {
    public static Map<Enchantment, Integer> doMerge(ItemStack top, ItemStack bottom) {
        Map<Enchantment, Integer> bottomEnchants = new HashMap<>();
        Map<Enchantment, Integer> topEnchants = new HashMap<>();

        Map<Enchantment, Integer> toKeep = new HashMap<>();

        if(top != null) {
            if(top.getItemMeta() instanceof EnchantmentStorageMeta) {
                topEnchants = new HashMap<>(((EnchantmentStorageMeta) top.getItemMeta()).getStoredEnchants());
            } else {
                topEnchants = new HashMap<>(top.getEnchantments());
            }
        }

        if(bottom != null) {
            if(bottom.getItemMeta() instanceof EnchantmentStorageMeta) {
                bottomEnchants = new HashMap<>(((EnchantmentStorageMeta) bottom.getItemMeta()).getStoredEnchants());
            } else {
                bottomEnchants = new HashMap<>(bottom.getEnchantments());
            }
        }

        bottomEnchants.forEach(((enchantment, integer) -> {
            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchantment);
                if(!ecoEnchant.isGrindstoneable()) toKeep.putIfAbsent(enchantment, integer);
            } else {
                if(enchantment.isCursed()) toKeep.putIfAbsent(enchantment, integer);
            }
        }));
        topEnchants.forEach(((enchantment, integer) -> {
            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchantment);
                if(!ecoEnchant.isGrindstoneable()) toKeep.putIfAbsent(enchantment, integer);
            } else {
                if(enchantment.isCursed()) toKeep.putIfAbsent(enchantment, integer);
            }
        }));

        return toKeep;
    }
}

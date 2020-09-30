package com.willfp.ecoenchants.enchantments.util;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.NumberUtils;

public class EnchantmentUtils {
    public static double getDamageMultiplier(EcoEnchant enchantment, int level) {
        return 1 + ((enchantment.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier") * level)/ 100);
    }

    public static boolean passedChance(EcoEnchant enchantment, int level) {
        return NumberUtils.randFloat(0, 1) < ((enchantment.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level") * level) / 100);
    }

    public static double getDamageBonus(EcoEnchant enchant, int level) {
        return (level * enchant.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level"));
    }
}

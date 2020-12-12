package com.willfp.ecoenchants.enchantments.util;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import com.willfp.ecoenchants.integrations.placeholder.PlaceholderEntry;
import com.willfp.ecoenchants.integrations.placeholder.PlaceholderManager;
import com.willfp.ecoenchants.util.NumberUtils;
import com.willfp.ecoenchants.util.StringUtils;

public class EnchantmentUtils {
    public static boolean passedChance(EcoEnchant enchantment, int level) {
        return NumberUtils.randFloat(0, 1) < ((enchantment.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level") * level) / 100);
    }

    public static void registerPlaceholders(EcoEnchant enchantment) {

        PlaceholderManager.registerPlaceholder(
                new PlaceholderEntry(enchantment.getPermissionName() + "_" + "enabled", (player) -> String.valueOf(enchantment.isEnabled()))
        );

        enchantment.getConfig().config.getKeys(true).forEach(string -> {
            String key = string.replaceAll("\\.", "_").replaceAll("-", "_");
            Object object = enchantment.getConfig().config.get(string);

            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(enchantment.getPermissionName() + "_" + key, (player) -> StringUtils.internalToString(object))
            );
        });

        if (enchantment.getConfig().config.get(EcoEnchants.CONFIG_LOCATION + "chance-per-level") != null) {
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(enchantment.getPermissionName() + "_" + "chance_per_level", (player) -> NumberUtils.format(enchantment.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level")))
            );
        }

        if (enchantment.getConfig().config.get(EcoEnchants.CONFIG_LOCATION + "multiplier") != null) {
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(enchantment.getPermissionName() + "_" + "multiplier", (player) -> NumberUtils.format(enchantment.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier")))
            );
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(enchantment.getPermissionName() + "_" + "multiplier_percentage", (player) -> NumberUtils.format(enchantment.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier") * 100))
            );
        }

        if (enchantment instanceof Spell) {
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(enchantment.getPermissionName() + "_" + "cooldown", (player) -> NumberUtils.format(Spell.getCooldown((Spell) enchantment, player)), true)
            );
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(enchantment.getPermissionName() + "_" + "cooldown_total", (player) -> NumberUtils.format(((Spell) enchantment).getCooldownTime()))
            );
        }
    }
}

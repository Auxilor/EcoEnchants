package com.willfp.ecoenchants.enchantments.custom;

import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class CustomEcoEnchants {
    /**
     * Custom EcoEnchants.
     */
    private static final Set<EcoEnchant> VALUES = new HashSet<>();

    /**
     * Update the map.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public static void update(@NotNull final EcoEnchantsPlugin plugin) {
        for (EcoEnchant enchant : VALUES) {
            EcoEnchants.removeEcoEnchant(enchant);
            EnchantmentUtils.unregister(enchant);
        }

        VALUES.clear();

        for (Config cfg : plugin.getCustomEnchantsYml().getSubsections("enchants")) {
            VALUES.add(new CustomEcoEnchant(cfg));
        }
    }
}

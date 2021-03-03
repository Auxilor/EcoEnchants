package com.willfp.ecoenchants.config;

import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.config.configs.Rarity;
import com.willfp.ecoenchants.config.configs.Target;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class EcoEnchantsConfigs {
    /**
     * target.yml.
     */
    public static final Target TARGET = new Target();

    /**
     * rarity.yml.
     */
    public static final Rarity RARITY = new Rarity();

    /**
     * All enchantment-specific configs.
     */
    @Getter
    private final Set<EnchantmentConfig> enchantmentConfigs = new HashSet<>();

    /**
     * Update all configs.
     */
    @ConfigUpdater
    public void updateConfigs() {
        TARGET.update();
        RARITY.update();
        enchantmentConfigs.forEach(EnchantmentConfig::update);
    }

    /**
     * Get EnchantmentConfig matching permission name.
     *
     * @param permissionName The permission name to match.
     * @return The matching {@link EnchantmentConfig}.
     */
    public EnchantmentConfig getEnchantmentConfig(@NotNull final String permissionName) {
        return enchantmentConfigs.stream().filter(config -> config.getName().equalsIgnoreCase(permissionName)).findFirst().orElse(null);
    }

    /**
     * Adds new enchantment config yml.
     *
     * @param config The config to add.
     */
    public void addEnchantmentConfig(@NotNull final EnchantmentConfig config) {
        enchantmentConfigs.add(config);
    }
}

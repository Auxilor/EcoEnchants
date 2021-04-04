package com.willfp.ecoenchants.config;

import com.willfp.eco.core.config.ConfigUpdater;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class EcoEnchantsConfigs {
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

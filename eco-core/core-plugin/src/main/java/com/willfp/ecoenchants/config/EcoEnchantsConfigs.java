package com.willfp.ecoenchants.config;

import com.willfp.eco.util.config.annotations.Updatable;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.config.configs.Rarity;
import com.willfp.ecoenchants.config.configs.Target;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@Updatable(methodName = "updateConfigs")
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
    public void updateConfigs() {
        TARGET.update();
        RARITY.update();
        enchantmentConfigs.forEach(EnchantmentYamlConfig::update);
    }

    /**
     * Get EnchantmentConfig matching permission name.
     *
     * @param permissionName The permission name to match.
     * @return The matching {@link EnchantmentConfig}.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public EnchantmentConfig getEnchantmentConfig(@NotNull final String permissionName) {
        return enchantmentConfigs.stream().filter(config -> config.getName().equalsIgnoreCase(permissionName)).findFirst().get();
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

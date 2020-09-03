package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.config.configs.Config;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.config.configs.Lang;
import com.willfp.ecoenchants.config.configs.Target;

import java.util.HashSet;
import java.util.Set;

public class ConfigManager {
    private static final Lang LANG = new Lang();
    private static final Config CONFIG = new Config();
    private static final Target TARGET = new Target();
    private static final Set<EnchantmentConfig> enchantmentConfigs = new HashSet<>();

    /**
     * Update all configs
     * Called on /ecoreload
     */
    public static void updateConfigs() {
        LANG.reload();
        CONFIG.reload();
        TARGET.reload();
        updateEnchantmentConfigs();
    }

    /**
     * Update enchantment configs
     */
    public static void updateEnchantmentConfigs() {
        enchantmentConfigs.forEach((EnchantmentYamlConfig::reload));
    }

    /**
     * Get all enchantment configs
     * @return Set of all enchantment configs
     */
    public static Set<EnchantmentConfig> getEnchantmentConfigs() {
        return enchantmentConfigs;
    }

    /**
     * Get EnchantmentConfig matching permission name
     * @param permissionName The permission name to match
     * @return The matching {@link EnchantmentConfig}
     */
    public static EnchantmentConfig getEnchantmentConfig(String permissionName) {
        return enchantmentConfigs.stream().filter(config -> config.getName().equalsIgnoreCase(permissionName)).findFirst().get();
    }

    /**
     * Adds new enchantment config yml
     * @param config The config to add
     */
    public static void addEnchantmentConfig(EnchantmentConfig config) {
        enchantmentConfigs.add(config);
    }

    /**
     * Get lang.yml
     * @return lang.yml
     */
    public static Lang getLang() {
        return LANG;
    }

    /**
     * Get config.yml
     * @return config.yml
     */
    public static Config getConfig() {
        return CONFIG;
    }

    /**
     * Get target.yml
     * @return target.yml
     */
    public static Target getTarget() {
        return TARGET;
    }
}
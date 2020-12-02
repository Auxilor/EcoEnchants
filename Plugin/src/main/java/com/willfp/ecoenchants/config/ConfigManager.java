package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.config.configs.Config;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.config.configs.Lang;
import com.willfp.ecoenchants.config.configs.Rarity;
import com.willfp.ecoenchants.config.configs.Target;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {
    public static final HashMap<String, Double> configVersions = new HashMap<String, Double>() {{
        put("target", 1.0);
        put("rarity", 1.0);
    }};

    private static final Lang LANG = new Lang();
    private static final Config CONFIG = new Config();
    private static final Target TARGET = new Target();
    private static final Rarity RARITY = new Rarity();
    private static final Set<EnchantmentConfig> enchantmentConfigs = new HashSet<>();

    /**
     * Update all configs
     * Called on /ecoreload
     */
    public static void updateConfigs() {
        LANG.update();
        CONFIG.update();
        TARGET.update();
        RARITY.update();
        updateEnchantmentConfigs();
    }

    /**
     * Update enchantment configs
     */
    public static void updateEnchantmentConfigs() {
        enchantmentConfigs.forEach((EnchantmentYamlConfig::update));
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
    @SuppressWarnings("OptionalGetWithoutIsPresent")
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

    /**
     * Get rarity.yml
     * @return rarity.yml
     */
    public static Rarity getRarity() {
        return RARITY;
    }
}
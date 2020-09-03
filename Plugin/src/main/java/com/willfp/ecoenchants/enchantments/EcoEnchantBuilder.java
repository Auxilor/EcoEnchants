package com.willfp.ecoenchants.enchantments;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;

import java.util.regex.Pattern;

public class EcoEnchantBuilder {
    public final String name;
    public final String key;
    public final String permission;
    public double configVersion;
    public final EnchantmentConfig config;
    public Class<?> plugin;
    public final EcoEnchant.EnchantmentType type;

    /**
     * Creates new EcoEnchantBuilder
     * Only used by base enchantments, do not use for extensions.
     *
     * @param key The enchantment key name
     * @param type The enchantment type
     * @param version The config version
     */
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type, double version) {
        this(key, type, version, EcoEnchantsPlugin.class);
    }

    /**
     * Creates new EcoEnchantBuilder for Extension
     * Use for extensions
     *
     * @param key The enchantment key name
     * @param type The enchantment type
     * @param version The config version
     * @param plugin The main class of extension
     */
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type, double version, Class<?> plugin) {
        if(Pattern.matches("[a-z_]", key)) throw new InvalidEnchantmentException("Key must only contain lowercase letters and underscores");
        this.key = key;
        this.permission = key.replace("_","");

        this.type = type;

        this.plugin = plugin;

        this.configVersion = version;

        ConfigManager.addEnchantmentConfig(new EnchantmentConfig(permission, configVersion, plugin, this.type));
        this.config = ConfigManager.getEnchantmentConfig(permission);

        this.name = config.getString("name");
    }
}

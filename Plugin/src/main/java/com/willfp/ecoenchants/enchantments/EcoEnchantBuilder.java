package com.willfp.ecoenchants.enchantments;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;

import java.util.regex.Pattern;

public class EcoEnchantBuilder {
    public final String name;
    public final String key;
    public final String permission;
    public final EnchantmentConfig config;
    public Class<?> plugin;
    public final EcoEnchant.EnchantmentType type;

    /**
     * Creates new EcoEnchantBuilder
     * Only used by base enchantments, do not use for extensions.
     *
     * @param key The enchantment key name
     * @param type The enchantment type
     */
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type) {
        this(key, type, EcoEnchantsPlugin.class);
    }

    /**
     * Creates new EcoEnchantBuilder for Extension
     * Use for extensions
     *
     * @param key The enchantment key name
     * @param type The enchantment type
     * @param plugin The main class of extension
     */
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type, Class<?> plugin) {
        if(Pattern.matches("[a-z_]", key)) throw new InvalidEnchantmentException("Key must only contain lowercase letters and underscores");
        this.key = key;
        this.permission = key.replace("_","");

        this.type = type;

        this.plugin = plugin;

        ConfigManager.addEnchantmentConfig(new EnchantmentConfig(permission, plugin, this.type));
        this.config = ConfigManager.getEnchantmentConfig(permission);

        this.name = config.getString("name");
    }
}

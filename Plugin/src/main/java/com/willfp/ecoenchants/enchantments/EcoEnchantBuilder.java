package com.willfp.ecoenchants.enchantments;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class EcoEnchantBuilder {
    public final String name;
    public final String key;
    public final String permission;
    public final Set<Material> target;
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
     * @param applicable The materials that the enchantment can be applied to
     * @param version The config version
     */
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type, Target.Applicable applicable, double version) {
        this(key, type, new Target.Applicable[]{applicable}, version);
    }

    /**
     * Creates new EcoEnchantBuilder
     * Use for extensions
     *
     * @param key The enchantment key name
     * @param type The enchantment type
     * @param applicable The materials that the enchantment can be applied to
     * @param version The config version
     * @param plugin The main class of extension
     */
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type, Target.Applicable applicable, double version, Class<?> plugin) {
        this(key, type, new Target.Applicable[]{applicable}, version, plugin);
    }

    /**
     * Creates new EcoEnchantBuilder
     * Only used by base enchantments, do not use for extensions.
     *
     * @param key The enchantment key name
     * @param type The enchantment type
     * @param applicable The materials that the enchantment can be applied to
     * @param version The config version
     */
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type, Target.Applicable[] applicable, double version) {
        this(key, type, applicable, version, Main.class);
    }

    /**
     * Creates new EcoEnchantBuilder for Extension
     * Use for extensions
     *
     * @param key The enchantment key name
     * @param type The enchantment type
     * @param applicable The materials that the enchantment can be applied to
     * @param version The config version
     * @param plugin The main class of extension
     */
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type, Target.Applicable[] applicable, double version, Class<?> plugin) {
        if(Pattern.matches("[a-z_]", key)) throw new InvalidEnchantmentException("Key must only contain lowercase letters and underscores");
        this.key = key;
        this.permission = key.replace("_","");

        Set<Material> target = new HashSet<>();
        Arrays.asList(applicable).forEach((applicable1 -> {
            target.addAll(applicable1.getMaterials());
        }));

        this.target = target;

        this.type = type;

        this.plugin = plugin;

        this.configVersion = version;

        ConfigManager.addEnchantmentConfig(new EnchantmentConfig(permission, configVersion, plugin, this.type));
        this.config = ConfigManager.getEnchantmentConfig(permission);

        this.name = config.getString("name");
    }

    /**
     * Creates new EcoEnchantBuilder for Extension
     * Use for extensions
     *
     * @param key The enchantment key name
     * @param type The enchantment type
     * @param target The materials that the enchantment can be applied to
     * @param version The config version
     * @param plugin The main class of extension
     *
     * @deprecated Use {@link Target.Applicable} instead
     */
    @Deprecated
    public EcoEnchantBuilder(String key, EcoEnchant.EnchantmentType type, Set<Material> target, double version, Class<?> plugin) {
        if(Pattern.matches("[a-z_]", key)) throw new InvalidEnchantmentException("Key must only contain lowercase letters and underscores");
        this.key = key;
        this.permission = key.replace("_","");

        this.target = target;

        this.type = type;

        this.plugin = plugin;

        this.configVersion = version;

        ConfigManager.addEnchantmentConfig(new EnchantmentConfig(permission, configVersion, plugin, this.type));
        this.config = ConfigManager.getEnchantmentConfig(permission);

        this.name = config.getString("name");
    }
}

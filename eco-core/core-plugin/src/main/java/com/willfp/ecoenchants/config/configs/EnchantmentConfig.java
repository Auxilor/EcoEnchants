package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.Configs;
import com.willfp.ecoenchants.config.EnchantmentYamlConfig;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Wrapper for enchantment-specific configs
 */
public class EnchantmentConfig extends EnchantmentYamlConfig {
    private final String name;

    public EnchantmentConfig(String name, Class<?> plugin, EnchantmentType type) {
        super(name, plugin, type);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    public List<Integer> getInts(String path) {
        return config.getIntegerList(path);
    }

    public boolean getBool(String path) {
        return config.getBoolean(path);
    }

    public boolean getBool(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    public List<Boolean> getBools(String path) {
        return config.getBooleanList(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStrings(String path) {
        return config.getStringList(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public List<Double> getDoubles(String path) {
        return config.getDoubleList(path);
    }

    public ItemStack getItemStack(String path) {
        return config.getItemStack(path);
    }

    public Set<Enchantment> getEnchantments(String path) {
        Set<Enchantment> enchantments = new HashSet<>();
        List<String> enchantmentKeys = config.getStringList(path);
        enchantmentKeys.forEach((key -> enchantments.add(Enchantment.getByKey(NamespacedKey.minecraft(key)))));
        return enchantments;
    }

    public EnchantmentRarity getRarity() {
        String rarityName = this.getString("obtaining.rarity");
        return EnchantmentRarity.getByName(rarityName);
    }

    public Set<EnchantmentTarget> getTargets() {
        List<String> targetNames = config.getStringList(EcoEnchants.GENERAL_LOCATION + "targets");
        if (targetNames == null || targetNames.isEmpty()) return new HashSet<>();
        Set<EnchantmentTarget> targets = new HashSet<>();

        targetNames.forEach((s -> {
            if (EnchantmentTarget.getByName(s) == null) {
                return;
            }
            targets.add(EnchantmentTarget.getByName(s));
        }));

        return targets;
    }

    public void loadFromLang() {
        if (!Configs.LANG.config.contains("enchantments." + this.getName()))
            return;

        config.set("name", Configs.LANG.getString("enchantments." + this.getName() + ".name"));
        config.set("description", Configs.LANG.getString("enchantments." + this.getName() + ".description"));
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

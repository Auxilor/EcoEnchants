package com.willfp.ecoenchants.config.configs;

import com.willfp.ecoenchants.config.EnchantmentYamlConfig;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EnchantmentRarity;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Wrapper for enchantment-specific configs
 */
public class EnchantmentConfig extends EnchantmentYamlConfig {
    private final String name;

    public EnchantmentConfig(String name, double version, Class<?> plugin, EcoEnchant.EnchantmentType type) {
        super(name, version, plugin, type);
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

    public Set<Material> getTarget(Set<Material> initialTarget) {
        List<String> targetName = config.getStringList("general-config.target");
        if(targetName == null || targetName.isEmpty()) return initialTarget;

        Set<Material> targets = new HashSet<>();
        targetName.forEach((s -> {
            Material mat = Material.getMaterial(s.toUpperCase());
            targets.add(mat);
        }));

        return targets;
    }
}

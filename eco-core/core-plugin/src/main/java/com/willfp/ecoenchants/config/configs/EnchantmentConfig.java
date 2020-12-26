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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Wrapper for enchantment-specific configs
 */
public class EnchantmentConfig extends EnchantmentYamlConfig {
    private final String name;

    public EnchantmentConfig(@NotNull final String name,
                             @NotNull final Class<?> plugin,
                             @NotNull final EnchantmentType type) {
        super(name, plugin, type);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getInt(@NotNull final String path) {
        return this.getConfig().getInt(path);
    }

    public int getInt(@NotNull final String path,
                      final int def) {
        return this.getConfig().getInt(path, def);
    }

    public List<Integer> getInts(@NotNull final String path) {
        return this.getConfig().getIntegerList(path);
    }

    public boolean getBool(@NotNull final String path) {
        return this.getConfig().getBoolean(path);
    }

    public boolean getBool(@NotNull final String path,
                           final boolean def) {
        return this.getConfig().getBoolean(path, def);
    }

    public List<Boolean> getBools(@NotNull final String path) {
        return this.getConfig().getBooleanList(path);
    }

    public String getString(@NotNull final String path) {
        return this.getConfig().getString(path);
    }

    public List<String> getStrings(@NotNull final String path) {
        return this.getConfig().getStringList(path);
    }

    public double getDouble(@NotNull final String path) {
        return this.getConfig().getDouble(path);
    }

    public List<Double> getDoubles(@NotNull final String path) {
        return this.getConfig().getDoubleList(path);
    }

    public ItemStack getItemStack(@NotNull final String path) {
        return this.getConfig().getItemStack(path);
    }

    public Set<Enchantment> getEnchantments(@NotNull final String path) {
        Set<Enchantment> enchantments = new HashSet<>();
        List<String> enchantmentKeys = this.getConfig().getStringList(path);
        enchantmentKeys.forEach((key -> enchantments.add(Enchantment.getByKey(NamespacedKey.minecraft(key)))));
        return enchantments;
    }

    public EnchantmentRarity getRarity() {
        String rarityName = this.getString("obtaining.rarity");
        return EnchantmentRarity.getByName(rarityName);
    }

    public Set<EnchantmentTarget> getTargets() {
        List<String> targetNames = this.getConfig().getStringList(EcoEnchants.GENERAL_LOCATION + "targets");
        if (targetNames.isEmpty()) {
            return new HashSet<>();
        }
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
        if (!Configs.LANG.getConfig().contains("enchantments." + this.getName())) {
            return;
        }

        this.getConfig().set("name", Configs.LANG.getString("enchantments." + this.getName() + ".name"));
        this.getConfig().set("description", Configs.LANG.getString("enchantments." + this.getName() + ".description"));
        try {
            this.getConfig().save(this.getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

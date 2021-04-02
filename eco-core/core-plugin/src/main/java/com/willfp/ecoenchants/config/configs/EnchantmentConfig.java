package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.ExtendableConfig;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnchantmentConfig extends ExtendableConfig {
    /**
     * The name of the config.
     */
    @Getter
    private final String name;

    /**
     * Instantiate a new config for an enchantment.
     *
     * @param name   The name of the config.
     * @param plugin The provider of the enchantment.
     * @param type   The {@link EnchantmentType} of the enchantment.
     */
    public EnchantmentConfig(@NotNull final String name,
                             @NotNull final Class<?> plugin,
                             @NotNull final EnchantmentType type) {
        super(name, true, EcoEnchantsPlugin.getInstance(), plugin, "enchants/" + type.getName() + "/");
        this.name = name;
    }

    /**
     * Get a set of enchantments stored by key.
     *
     * @param path The location of the enchantments in the config.
     * @return A set of all enchantments.
     */
    public Set<Enchantment> getEnchantments(@NotNull final String path) {
        Set<Enchantment> enchantments = new HashSet<>();
        List<String> enchantmentKeys = this.getStrings(path);
        enchantmentKeys.forEach((key -> enchantments.add(Enchantment.getByKey(NamespacedKey.minecraft(key)))));
        return enchantments;
    }

    /**
     * Get the rarity of the enchantment.
     *
     * @return The rarity, or null if invalid.
     */
    public EnchantmentRarity getRarity() {
        String rarityName = this.getString("obtaining.rarity");
        return EnchantmentRarity.getByName(rarityName);
    }

    /**
     * Get all applicable targets.
     *
     * @return The targets.
     */
    public Set<EnchantmentTarget> getTargets() {
        List<String> targetNames = this.getStrings(EcoEnchants.GENERAL_LOCATION + "targets");
        if (targetNames.isEmpty()) {
            return new HashSet<>();
        }
        Set<EnchantmentTarget> targets = new HashSet<>();

        targetNames.forEach((s -> {
            if (EnchantmentTarget.getByName(s) == null) {
                Bukkit.getLogger().warning("Target specified in " + name + " is invalid!");
                return;
            }
            targets.add(EnchantmentTarget.getByName(s));
        }));

        return targets;
    }

    /**
     * Load config values from lang.yml.
     */
    public void loadFromLang() {
        if (!this.getPlugin().getLangYml().has("enchantments." + this.getName())) {
            return;
        }

        this.config.set("name", this.getPlugin().getLangYml().getString("enchantments." + this.getName() + ".name"));
        this.config.set("description", this.getPlugin().getLangYml().getString("enchantments." + this.getName() + ".description"));
        try {
            this.config.save(this.getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

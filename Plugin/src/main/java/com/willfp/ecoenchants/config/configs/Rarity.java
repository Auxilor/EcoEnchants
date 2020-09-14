package com.willfp.ecoenchants.config.configs;

import com.willfp.ecoenchants.config.YamlConfig;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper for config.yml
 */
public class Rarity extends YamlConfig {
    public Rarity() {
        super("rarity");
    }

    public Set<String> getRarities() {
        return config.getConfigurationSection("rarities").getKeys(false);
    }
}

package com.willfp.ecoenchants.config.configs;

import com.willfp.ecoenchants.config.RootConfig;

import java.util.Set;

/**
 * Wrapper for config.yml
 */
public class Rarity extends RootConfig {
    public Rarity() {
        super("rarity");
    }

    public Set<String> getRarities() {
        return config.getConfigurationSection("rarities").getKeys(false);
    }
}

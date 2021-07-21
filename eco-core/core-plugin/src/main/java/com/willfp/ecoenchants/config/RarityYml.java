package com.willfp.ecoenchants.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.yaml.YamlBaseConfig;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RarityYml extends YamlBaseConfig {
    /**
     * Instantiate rarity.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public RarityYml(@NotNull final EcoPlugin plugin) {
        super("rarity", false, plugin);
    }

    /**
     * Get all rarity names.
     *
     * @return Set of all rarity names.
     */
    public List<String> getRarities() {
        return this.getSubsection("rarities").getKeys(false);
    }
}

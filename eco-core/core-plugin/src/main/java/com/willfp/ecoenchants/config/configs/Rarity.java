package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.BaseConfig;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Wrapper for config.yml
 */
public class Rarity extends BaseConfig {
    public Rarity() {
        super("rarity", false);
    }

    public Set<String> getRarities() {
        return config.getConfigurationSection("rarities").getKeys(false);
    }

    public int getInt(@NotNull String path) {
        return config.getInt(path);
    }

    public int getInt(@NotNull String path, int def) {
        return config.getInt(path, def);
    }

    public @NotNull List<Integer> getInts(@NotNull String path) {
        return config.getIntegerList(path);
    }

    public boolean getBool(@NotNull String path) {
        return config.getBoolean(path);
    }

    public @NotNull List<Boolean> getBools(@NotNull String path) {
        return config.getBooleanList(path);
    }

    public @NotNull String getString(@NotNull String path) {
        return config.getString(path);
    }

    public @NotNull List<String> getStrings(@NotNull String path) {
        return config.getStringList(path);
    }

    public double getDouble(@NotNull String path) {
        return config.getDouble(path);
    }

    public @NotNull List<Double> getDoubles(@NotNull String path) {
        return config.getDoubleList(path);
    }
}

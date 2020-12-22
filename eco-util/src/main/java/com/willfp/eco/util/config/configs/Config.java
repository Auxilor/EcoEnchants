package com.willfp.eco.util.config.configs;

import com.willfp.eco.util.config.BaseConfig;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Wrapper for config.yml
 */
public class Config extends BaseConfig {
    public Config() {
        super("config", true);
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
}

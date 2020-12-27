package com.willfp.eco.util.config;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public abstract class BaseConfig extends PluginDependent {
    /**
     * The linked {@link YamlConfiguration} where values are physically stored.
     */
    @Getter(AccessLevel.PUBLIC)
    protected final YamlConfiguration config;

    /**
     * The physical config file, as stored on disk.
     */
    private final File configFile;

    /**
     * The full name of the config file (eg config.yml).
     */
    private final String name;

    /**
     * Whether keys not in the base config should be removed on update.
     */
    private final boolean removeUnused;

    /**
     * Config implementation for configs present in the plugin's base directory (eg config.yml, lang.yml).
     * <p>
     * Automatically updates.
     *
     * @param configName   The name of the config
     * @param removeUnused Whether keys not present in the default config should be removed on update.
     */
    protected BaseConfig(@NotNull final String configName,
                         final boolean removeUnused) {
        super(AbstractEcoPlugin.getInstance());
        this.name = configName + ".yml";
        this.removeUnused = removeUnused;

        if (!new File(this.getPlugin().getDataFolder(), this.name).exists()) {
            createFile();
        }

        this.configFile = new File(this.getPlugin().getDataFolder(), this.name);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        update();
    }

    private void createFile() {
        this.getPlugin().saveResource(name, false);
    }

    /**
     * Update the config.
     * <p>
     * Writes missing values, however removes comments due to how configs are stored internally in bukkit.
     */
    public void update() {
        try {
            config.load(configFile);

            InputStream newIn = this.getPlugin().getResource(name);
            if (newIn == null) {
                this.getPlugin().getLog().error(name + " is null?");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(newIn, StandardCharsets.UTF_8));
            YamlConfiguration newConfig = new YamlConfiguration();
            newConfig.load(reader);

            if (newConfig.getKeys(true).equals(config.getKeys(true))) {
                return;
            }

            newConfig.getKeys(true).forEach((s -> {
                if (!config.getKeys(true).contains(s)) {
                    config.set(s, newConfig.get(s));
                }
            }));

            if (this.removeUnused) {
                config.getKeys(true).forEach((s -> {
                    if (!newConfig.getKeys(true).contains(s)) {
                        config.set(s, null);
                    }
                }));
            }

            config.save(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get an integer from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or 0 if not found.
     */
    public int getInt(@NotNull final String path) {
        return config.getInt(path, 0);
    }

    /**
     * Get an integer from config with a specified default (not found) value.
     *
     * @param path The key to fetch the value from.
     * @param def  The value to default to if not found.
     * @return The found value, or the default.
     */
    public int getInt(@NotNull final String path,
                      final int def) {
        return config.getInt(path, def);
    }

    /**
     * Get a list of integers from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or a blank {@link java.util.ArrayList} if not found.
     */
    @NotNull
    public List<Integer> getInts(@NotNull final String path) {
        return config.getIntegerList(path);
    }

    /**
     * Get a boolean from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or false if not found.
     */
    public boolean getBool(@NotNull final String path) {
        return config.getBoolean(path, false);
    }

    /**
     * Get a list of booleans from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or a blank {@link java.util.ArrayList} if not found.
     */
    @NotNull
    public List<Boolean> getBools(@NotNull final String path) {
        return config.getBooleanList(path);
    }

    /**
     * Get a string from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or an empty string if not found.
     */
    @NotNull
    public String getString(@NotNull final String path) {
        return Objects.requireNonNull(config.getString(path, ""));
    }

    /**
     * Get a list of strings from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or a blank {@link java.util.ArrayList} if not found.
     */
    @NotNull
    public List<String> getStrings(@NotNull final String path) {
        return config.getStringList(path);
    }

    /**
     * Get a decimal from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or 0 if not found.
     */
    public double getDouble(@NotNull final String path) {
        return config.getDouble(path, 0);
    }

    /**
     * Get a list of decimals from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or a blank {@link java.util.ArrayList} if not found.
     */
    @NotNull
    public List<Double> getDoubles(@NotNull final String path) {
        return config.getDoubleList(path);
    }
}

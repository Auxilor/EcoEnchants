package com.willfp.ecoenchants.config;

import com.willfp.eco.util.config.ValueGetter;
import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * Class implemented by enchantment configs
 */
public abstract class EnchantmentYamlConfig extends PluginDependent implements ValueGetter {
    private final String name;

    @Getter
    private YamlConfiguration config;

    @Getter(AccessLevel.PROTECTED)
    private File configFile;
    private final File directory;
    private final Class<?> source;
    private final EnchantmentType type;

    /**
     * Create new config yml
     *
     * @param name   The config name
     * @param source The class of the main class of source or extension
     * @param type   The enchantment type
     */
    protected EnchantmentYamlConfig(@NotNull final String name,
                                    @NotNull final Class<?> source,
                                    @NotNull final EnchantmentType type) {
        super(AbstractEcoPlugin.getInstance());
        this.name = name;
        this.source = source;
        this.type = type;

        File basedir = new File(this.getPlugin().getDataFolder(), "enchants/");
        if (!basedir.exists()) {
            basedir.mkdirs();
        }

        File dir = new File(basedir, type.getName() + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.directory = dir;

        if (!new File(directory, name + ".yml").exists()) {
            createFile();
        }

        this.configFile = new File(directory, name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);

        update();
    }

    private void saveResource() {
        String resourcePath = "/enchants/" + type.getName() + "/" + name + ".yml";

        InputStream in = source.getResourceAsStream(resourcePath);

        File outFile = new File(this.getPlugin().getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(this.getPlugin().getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists()) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (IOException ignored) {
        }
    }

    private void createFile() {
        saveResource();
    }

    public void update() {
        try {
            config.load(configFile);

            String resourcePath = "/enchants/" + type.getName() + "/" + name + ".yml";
            InputStream newIn = source.getResourceAsStream(resourcePath);

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

            config.getKeys(true).forEach((s -> {
                if (!newConfig.getKeys(true).contains(s)) {
                    config.set(s, null);
                }
            }));

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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public boolean getBool(@NotNull final String path) {
        return config.getBoolean(path, false);
    }

    /**
     * Get a list of booleans from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or a blank {@link java.util.ArrayList} if not found.
     */
    @Override
    @NotNull
    public List<Boolean> getBools(@NotNull final String path) {
        return config.getBooleanList(path);
    }

    /**
     * Get a string from config.C
     *
     * @param path The key to fetch the value from.
     * @return The found value, or an empty string if not found.
     */
    @Override
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
    @Override
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
    @Override
    public double getDouble(@NotNull final String path) {
        return config.getDouble(path, 0);
    }

    /**
     * Get a list of decimals from config.
     *
     * @param path The key to fetch the value from.
     * @return The found value, or a blank {@link java.util.ArrayList} if not found.
     */
    @Override
    @NotNull
    public List<Double> getDoubles(@NotNull final String path) {
        return config.getDoubleList(path);
    }
}

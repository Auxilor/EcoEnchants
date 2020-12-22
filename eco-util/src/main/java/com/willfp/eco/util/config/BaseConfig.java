package com.willfp.eco.util.config;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class BaseConfig extends PluginDependent {
    public final YamlConfiguration config;
    private final File configFile;
    private final String name;
    private final boolean removeUnused;

    protected BaseConfig(String configName, boolean removeUnused) {
        super(AbstractEcoPlugin.getInstance());
        this.name = configName + ".yml";
        this.removeUnused = removeUnused;

        if (!new File(plugin.getDataFolder(), this.name).exists()) {
            createFile();
        }

        this.configFile = new File(plugin.getDataFolder(), this.name);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        update();
    }

    private void createFile() {
        plugin.saveResource(name, false);
    }

    public void update() {
        try {
            config.load(configFile);

            InputStream newIn = plugin.getResource(name);
            if (newIn == null) {
                plugin.getLog().error(name + " is null?");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(newIn, StandardCharsets.UTF_8));
            YamlConfiguration newConfig = new YamlConfiguration();
            newConfig.load(reader);

            if (newConfig.getKeys(true).equals(config.getKeys(true)))
                return;

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
}

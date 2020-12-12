package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class UpdatingYamlConfig {
    public YamlConfiguration config;
    private File configFile;
    private final String name;
    private final boolean removeUnused;

    public UpdatingYamlConfig(String name, boolean removeUnused) {
        this.name = name + ".yml";
        this.removeUnused = removeUnused;
        init();
    }

    private void init() {
        if (!new File(EcoEnchantsPlugin.getInstance().getDataFolder(), name).exists()) {
            createFile();
        }

        this.configFile = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), name);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        update();
    }

    private void createFile() {
        EcoEnchantsPlugin.getInstance().saveResource(name, false);
    }

    public void update() {
        try {
            config.load(configFile);

            InputStream newIn = EcoEnchantsPlugin.getInstance().getResource(name);
            if (newIn == null) {
                Logger.error(name + " is null?");
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

package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class UpdatingLang {
    public YamlConfiguration config;
    private File configFile;

    public UpdatingLang() {
        init();
    }

    private void init() {
        if (!new File(EcoEnchantsPlugin.getInstance().getDataFolder(), "lang.yml").exists()) {
            createFile();
        }

        this.configFile = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), "lang.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);

        update();
    }

    private void createFile() {
        EcoEnchantsPlugin.getInstance().saveResource("lang.yml", false);
    }

    public void update() {
        try {
            InputStream newIn = EcoEnchantsPlugin.getInstance().getResource("lang.yml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(newIn, StandardCharsets.UTF_8));
            YamlConfiguration newConfig = new YamlConfiguration();
            newConfig.load(reader);

            newConfig.getKeys(true).forEach((s -> {
                if (!config.getKeys(true).contains(s)) {
                    config.set(s, newConfig.get(s));
                }
            }));

            config.save(configFile);
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}

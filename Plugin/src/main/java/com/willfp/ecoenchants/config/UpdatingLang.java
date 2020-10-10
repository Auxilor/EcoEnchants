package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;
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
            config.load(configFile);
            Bukkit.getLogger().info("BEFORE: " + config.getKeys(true).toString());

            InputStream newIn = EcoEnchantsPlugin.getInstance().getResource("lang.yml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(newIn, StandardCharsets.UTF_8));
            YamlConfiguration newConfig = new YamlConfiguration();
            newConfig.load(reader);
            Bukkit.getLogger().info("NEW: " + newConfig.getKeys(true).toString());

            newConfig.getKeys(true).forEach((s -> {
                if (!config.getKeys(true).contains(s)) {
                    config.set(s, newConfig.get(s));
                }
            }));
            Bukkit.getLogger().info("AFTER: " + config.getKeys(true).toString());

            config.save(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}

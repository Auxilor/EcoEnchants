package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class YamlConfig {
    private final String name;
    public YamlConfiguration config;
    private File configFile;

    public YamlConfig(String name) {
        this.name = name;

        init();
    }

    private void init() {
        if (!new File(EcoEnchantsPlugin.getInstance().getDataFolder(), name + ".yml").exists()) {
            createFile();
        }

        this.configFile = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);

        checkVersion();
    }

    private void createFile() {
        EcoEnchantsPlugin.getInstance().saveResource(name + ".yml", false);
    }

    private void replaceFile() {
        EcoEnchantsPlugin.getInstance().saveResource(name + ".yml", true);
    }

    public void reload() {
        try {
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Logger.error("§cCould not reload " + name + ".yml - Contact Auxilor.");
        }
    }

    private void checkVersion() {
        double latestVersion = ConfigManager.configVersions.get(this.name);
        if (latestVersion != config.getDouble("config-version")) {
            Logger.warn("EcoEnchants detected an older or invalid " + name + ".yml. Replacing it with the default config...");
            Logger.warn("If you've edited the config, copy over your changes!");
            performOverwrite();
            Logger.info("§aReplacement complete!");
        }
    }

    private void performOverwrite() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = dtf.format(now);
        try {
            File backupDir = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), "backup/");
            if(!backupDir.exists()) backupDir.mkdirs();
            File oldConf = new File(backupDir, name + "_" + dateTime + ".yml");
            oldConf.createNewFile();
            FileInputStream fis = new FileInputStream(EcoEnchantsPlugin.getInstance().getDataFolder() + "/" + name + ".yml");
            FileOutputStream fos = new FileOutputStream(oldConf);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fis.close();
            fos.close();
            replaceFile();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("§cCould not update config. Try reinstalling EcoEnchants");
        }
    }
}

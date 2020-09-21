package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractConfig {
    private static AbstractConfig instance;

    private final String name;
    private final File directory;
    private final double latestVersion;
    private final EcoEnchantsPlugin PLUGIN = EcoEnchantsPlugin.getInstance();
    private final Class<?> sourceClass;

    protected final File configFile;
    protected final YamlConfiguration config;

    protected AbstractConfig(String name, File directory, Class<?> sourceClass, double latestVersion) {
        this.name = name;
        this.directory = directory;
        this.sourceClass = sourceClass;
        this.latestVersion = latestVersion;

        if(!directory.exists()) directory.mkdirs();

        this.configFile = new File(directory, name + ".yml");

        if(!this.configFile.exists()) {
            createFile();
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);

        instance = this;

        checkVersion();
    }

    private void saveResource(boolean replace) {
        String resourcePath = configFile.getPath().replace(PLUGIN.getDataFolder().getPath(), "");

        InputStream in =  sourceClass.getResourceAsStream(resourcePath);

        File outFile = new File(PLUGIN.getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(PLUGIN.getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (IOException ignored) {}
    }

    private void createFile() {
        saveResource(false);
    }
    private void replaceFile() {
        saveResource(true);
    }

    public void reload() {
        try {
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("§cCould not reload " + name + ".yml - Contact Auxilor.");
        }
    }

    private void checkVersion() {
        if (latestVersion != config.getDouble("config-version")) {
            Bukkit.getLogger().warning("EcoEnchants detected an older or invalid " + name + ".yml. Replacing it with the default config...");
            Bukkit.getLogger().warning("If you've edited the config, copy over your changes!");
            performOverwrite();
            Bukkit.getLogger().info("§aReplacement complete!");
        }
    }

    private void performOverwrite() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = dtf.format(now);
        try {
            File backupDir = new File(PLUGIN.getDataFolder(), "backup/");
            if(!backupDir.exists()) backupDir.mkdirs();
            File oldConf = new File(backupDir, name + "_" + dateTime + ".yml");
            oldConf.createNewFile();
            FileInputStream fis = new FileInputStream(directory + "/" + name + ".yml");
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
            Bukkit.getLogger().severe("§cCould not update config. Try reinstalling EcoEnchants");
        }
    }

    public static AbstractConfig getInstance() {
        return instance;
    }
}

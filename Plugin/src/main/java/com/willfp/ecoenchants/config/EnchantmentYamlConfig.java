package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class implemented by enchantment configs
 */
public abstract class EnchantmentYamlConfig {

    private final String name;
    public YamlConfiguration config;
    private File configFile;
    private final File directory;
    private final double latestVersion;
    private final JavaPlugin plugin = Main.getInstance();
    private final Class<?> plugin2;
    private final EcoEnchant.EnchantmentType type;
    private File basedir = new File(this.plugin.getDataFolder(), "enchants/");

    /**
     * Create new config yml
     *
     * @param name The config name
     * @param latestVersion The latest config version
     * @param plugin The class of the main class of plugin or extension
     * @param type The enchantment type
     */
    public EnchantmentYamlConfig(String name, double latestVersion, Class<?> plugin, EcoEnchant.EnchantmentType type) {
        this.name = name;
        this.latestVersion = latestVersion;
        this.plugin2 = plugin;
        this.type = type;

        if(!basedir.exists()) basedir.mkdirs();

        File dir = new File(basedir, type.name().toLowerCase() + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.directory = dir;

        init();
    }

    private void init() {
        if (!new File(directory, name + ".yml").exists()) {
            createFile();
        }

        this.configFile = new File(directory, name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);

        checkVersion();
    }

    private void saveResource(boolean replace) {
        String resourcePath = "/enchants/" + type.name().toLowerCase() + "/" + name + ".yml";

        InputStream in =  plugin2.getResourceAsStream(resourcePath);

        File outFile = new File(Main.getInstance().getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(Main.getInstance().getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

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
            File backupDir = new File(plugin.getDataFolder(), "backup/");
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
}

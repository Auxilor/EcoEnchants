package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Class implemented by enchantment configs
 */
public abstract class EnchantmentYamlConfig {

    private final String name;
    public YamlConfiguration config;
    protected File configFile;
    private final File directory;
    private final JavaPlugin plugin = EcoEnchantsPlugin.getInstance();
    private final Class<?> plugin2;
    private final EcoEnchant.EnchantmentType type;
    private final File basedir = new File(this.plugin.getDataFolder(), "enchants/");

    /**
     * Create new config yml
     *
     * @param name The config name
     * @param plugin The class of the main class of plugin or extension
     * @param type The enchantment type
     */
    public EnchantmentYamlConfig(String name, Class<?> plugin, EcoEnchant.EnchantmentType type) {
        this.name = name;
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

        update();
    }

    private void saveResource(boolean replace) {
        String resourcePath = "/enchants/" + type.name().toLowerCase() + "/" + name + ".yml";

        InputStream in =  plugin2.getResourceAsStream(resourcePath);

        File outFile = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

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

    public void update() {
        try {
            config.load(configFile);

            String resourcePath = "/enchants/" + type.name().toLowerCase() + "/" + name + ".yml";
            InputStream newIn =  plugin2.getResourceAsStream(resourcePath);

            BufferedReader reader = new BufferedReader(new InputStreamReader(newIn, StandardCharsets.UTF_8));
            YamlConfiguration newConfig = new YamlConfiguration();
            newConfig.load(reader);

            if(newConfig.getKeys(true).equals(config.getKeys(true)))
                return;

            newConfig.getKeys(true).forEach((s -> {
                if (!config.getKeys(true).contains(s)) {
                    config.set(s, newConfig.get(s));
                }
            }));

            config.getKeys(true).forEach((s -> {
                if(!newConfig.getKeys(true).contains(s)) {
                    config.set(s, null);
                }
            }));

            config.save(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}

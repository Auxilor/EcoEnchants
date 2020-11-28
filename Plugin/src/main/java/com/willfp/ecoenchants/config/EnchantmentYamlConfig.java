package com.willfp.ecoenchants.config;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Class implemented by enchantment configs
 */
public abstract class EnchantmentYamlConfig {

    private final String name;
    public YamlConfiguration config;
    protected File configFile;
    private final File directory;
    private final Class<?> source;
    private final EcoEnchant.EnchantmentType type;

    /**
     * Create new config yml
     *
     * @param name The config name
     * @param plugin The class of the main class of plugin or extension
     * @param type The enchantment type
     */
    public EnchantmentYamlConfig(String name, Class<?> plugin, EcoEnchant.EnchantmentType type) {
        this.name = name;
        this.source = plugin;
        this.type = type;

        File basedir = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), "enchants/");
        if(!basedir.exists()) basedir.mkdirs();

        File dir = new File(basedir, type.getName() + "/");
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
        String resourcePath = "/enchants/" + type.getName() + "/" + name + ".yml";

        InputStream in =  source.getResourceAsStream(resourcePath);

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

            String resourcePath = "/enchants/" + type.getName() + "/" + name + ".yml";
            InputStream newIn =  source.getResourceAsStream(resourcePath);

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

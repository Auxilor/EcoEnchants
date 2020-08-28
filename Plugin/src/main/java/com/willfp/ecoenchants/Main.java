package com.willfp.ecoenchants;

import com.comphenix.protocol.ProtocolManager;
import com.willfp.ecoenchants.loader.Loader;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * The Main class for EcoEnchants
 */
public class Main extends JavaPlugin {
    private static Main instance;

    /**
     * Config versions for config.yml and lang.yml
     */
    public static final HashMap<String, Double> configVersions = new HashMap<String, Double>() {{
        put("config", 4.1);
        put("lang", 4.01);
    }};

    /**
     * Is the plugin outdated
     */
    public static boolean outdated;

    /**
     * Newest available plugin version
     */
    public static String newVersion;

    /**
     * Has AdvancedEnchantments?
     */
    public static boolean hasAE;

    /**
     * ProtocolLib
     */
    public ProtocolManager protocolManager;

    /**
     * Calls {@link Loader#load()}
     */
    public void onEnable() {
        Loader.load();
    }

    /**
     * Calls {@link Loader#unload()}
     */
    public void onDisable() {
        Loader.unload();
    }

    /**
     * Sets instance
     */
    public void onLoad() {
        instance = this;
    }

    /**
     * Get plugin instance
     * @return Plugin instance
     */
    public static Main getInstance() {
        return instance;
    }
}

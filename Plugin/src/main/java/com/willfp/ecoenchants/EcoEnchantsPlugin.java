package com.willfp.ecoenchants;

import com.comphenix.protocol.ProtocolManager;
import com.willfp.ecoenchants.loader.Loader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Main class for EcoEnchants
 */
public class EcoEnchantsPlugin extends JavaPlugin {
    private static EcoEnchantsPlugin instance;

    /**
     * Is the plugin outdated
     */
    public static boolean outdated;

    /**
     * Newest available plugin version
     */
    public static String newVersion;

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
    public static EcoEnchantsPlugin getInstance() {
        return instance;
    }
}

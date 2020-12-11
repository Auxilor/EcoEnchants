package com.willfp.ecoenchants;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.willfp.ecoenchants.extensions.loader.EcoExtensionLoader;
import com.willfp.ecoenchants.extensions.loader.ExtensionLoader;
import com.willfp.ecoenchants.util.internal.Loader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Main class for EcoEnchants
 */
public class EcoEnchantsPlugin extends JavaPlugin {
    /**
     * Instance of EcoEnchants
     */
    private static EcoEnchantsPlugin instance;

    /**
     * Extension loader
     */
    private final ExtensionLoader loader = new EcoExtensionLoader();

    /**
     * ProtocolLib
     */
    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    /**
     * NMS version
     */
    public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

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
     * Get extension loader
     * @return The {@link ExtensionLoader} attached to EcoEnchants
     */
    public ExtensionLoader getExtensionLoader() {
        return loader;
    }

    /**
     * Get ProtocolLib protocol manager
     * @return The {@link ProtocolManager} that EcoEnchants interacts with
     */
    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    /**
     * Get plugin instance
     * @return Plugin instance
     */
    public static EcoEnchantsPlugin getInstance() {
        return instance;
    }
}

package com.willfp.ecoenchants.extensions;

/**
 * Extensions are a way of interfacing with EcoEnchants
 * Syntactically similar to Bukkit Plugins.
 */
public abstract class Extension {
    /**
     * Create new Extension
     */
    public Extension() {
        onEnable();
    }

    /**
     * Called on enabling Extension
     */
    public abstract void onEnable();

    /**
     * Called when Extension is disabled
     */
    public abstract void onDisable();
}

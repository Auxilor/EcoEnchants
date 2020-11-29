package com.willfp.ecoenchants.util.internal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * Checks spigot if EcoEnchants is out of date
 */
public class UpdateChecker {
    private static boolean outdated;
    private static String newVersion;

    private final Plugin plugin;
    private final int resourceId;

    /**
     * Create an update checker for the specified spigot resource id
     *
     * @param plugin     The plugin to check
     * @param resourceId The resource ID of the plugin
     */
    public UpdateChecker(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    /**
     * Get the latest version of the plugin
     *
     * @param consumer The process to run after checking
     */
    public void getVersion(final Consumer<? super String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                this.plugin.getLogger().warning("Failed to check for EcoEnchants updates: " + exception.getMessage());
            }
        });
    }

    /**
     * Get if the plugin is outdated
     *
     * @return If the plugin is outdated
     */
    public static boolean isOutdated() {
        return outdated;
    }

    /**
     * Get the newest available version of the plugin
     *
     * @return The latest version
     */
    public static String getNewVersion() {
        return newVersion;
    }

    /**
     * Mark the plugin as outdated or not
     *
     * @param outdated Whether the plugin is outdated
     */
    public static void setOutdated(boolean outdated) {
        UpdateChecker.outdated = outdated;
    }

    /**
     * Set the newest available version of the plugin
     *
     * @param newVersion The newest version
     */
    public static void setNewVersion(String newVersion) {
        UpdateChecker.newVersion = newVersion;
    }
}
 
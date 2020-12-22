package com.willfp.eco.util.updater;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * Checks spigot if a plugin is out of date
 */
public class UpdateChecker {
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
                this.plugin.getLogger().warning("Failed to check for updates: " + exception.getMessage());
            }
        });
    }
}
 
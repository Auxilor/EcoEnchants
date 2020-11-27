package com.willfp.ecoenchants.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {

    private static boolean outdated;
    private static String newVersion;

    private final Plugin plugin;
    private final int resourceId;

    public UpdateChecker(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

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

    public static boolean isOutdated() {
        return outdated;
    }

    public static String getNewVersion() {
        return newVersion;
    }

    public static void setOutdated(boolean outdated) {
        UpdateChecker.outdated = outdated;
    }

    public static void setNewVersion(String newVersion) {
        UpdateChecker.newVersion = newVersion;
    }
}
 
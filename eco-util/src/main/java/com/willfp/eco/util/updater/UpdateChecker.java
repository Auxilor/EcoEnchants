package com.willfp.eco.util.updater;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
    /**
     * The instance of the plugin to check updates for.
     */
    @Getter(AccessLevel.PRIVATE)
    private final AbstractEcoPlugin plugin;

    /**
     * Create an update checker for the specified spigot resource id.
     *
     * @param plugin The plugin to check.
     */
    public UpdateChecker(@NotNull final AbstractEcoPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the latest version of the plugin.
     *
     * @param consumer The process to run after checking.
     */
    public void getVersion(@NotNull final Consumer<? super String> consumer) {
        this.getPlugin().getScheduler().runAsync(() -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.getPlugin().getResourceId()).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                this.getPlugin().getLogger().warning("Failed to check for updates: " + exception.getMessage());
            }
        });
    }
}

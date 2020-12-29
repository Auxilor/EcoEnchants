package com.willfp.eco.util.updater;

import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker extends PluginDependent {
    /**
     * Create an update checker for the specified spigot resource id.
     *
     * @param plugin The plugin to check.
     */
    public UpdateChecker(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Get the latest version of the plugin.
     *
     * @param consumer The process to run after checking.
     */
    public void getVersion(@NotNull final Consumer<? super String> consumer) {
        this.getPlugin().getScheduler().runAsync(() -> {
            try {
                InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.getPlugin().getResourceId()).openStream();
                Scanner scanner = new Scanner(inputStream);

                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException e) {
                this.getPlugin().getLogger().warning("Failed to check for updates: " + e.getMessage());
            }
        });
    }
}

package com.willfp.eco.util.bukkit.logging;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.jetbrains.annotations.NotNull;

public class EcoLogger extends PluginDependent implements Logger {
    /**
     * Implementation of {@link Logger}.
     * Manages logging for an {@link AbstractEcoPlugin}.
     *
     * @param plugin The {@link AbstractEcoPlugin} for the logger to manage.
     */
    public EcoLogger(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Log an informative/neutral message to console.
     *
     * @param message The message to log.
     */
    @Override
    public void info(@NotNull final String message) {
        this.getPlugin().getLogger().info(StringUtils.translate(message));
    }

    /**
     * Log a warning to console.
     *
     * @param message The message to log.
     */
    @Override
    public void warn(@NotNull final String message) {
        this.getPlugin().getLogger().warning(StringUtils.translate(message));
    }

    /**
     * Log an error to console.
     *
     * @param message The message to log.
     */
    @Override
    public void error(@NotNull final String message) {
        this.getPlugin().getLogger().severe(StringUtils.translate(message));
    }
}

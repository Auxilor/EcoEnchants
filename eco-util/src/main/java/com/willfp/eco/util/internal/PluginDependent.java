package com.willfp.eco.util.internal;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public abstract class PluginDependent {
    /**
     * The {@link AbstractEcoPlugin} that is stored.
     */
    @Getter(AccessLevel.PROTECTED)
    private final AbstractEcoPlugin plugin;

    /**
     * Pass an {@link AbstractEcoPlugin} in order to interface with it.
     *
     * @param plugin The plugin to manage.
     */
    protected PluginDependent(@NotNull final AbstractEcoPlugin plugin) {
        this.plugin = plugin;
    }
}

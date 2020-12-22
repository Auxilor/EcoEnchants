package com.willfp.eco.util.bukkit.keys;

import com.willfp.eco.util.factory.PluginDependentFactory;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class NamespacedKeyFactory extends PluginDependentFactory {
    /**
     * Factory class to produce {@link NamespacedKey}s associated with an {@link AbstractEcoPlugin}.
     *
     * @param plugin The plugin that this factory creates keys for.
     */
    public NamespacedKeyFactory(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Create an {@link NamespacedKey} associated with an {@link AbstractEcoPlugin}.
     *
     * @param key The key in the {@link NamespacedKey}.
     * @return The created {@link NamespacedKey}.
     */
    public NamespacedKey create(@NotNull final String key) {
        return new NamespacedKey(this.getPlugin(), key);
    }
}

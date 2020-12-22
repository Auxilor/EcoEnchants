package com.willfp.eco.util.bukkit.meta;

import com.willfp.eco.util.factory.PluginDependentFactory;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class MetadataValueFactory extends PluginDependentFactory {
    /**
     * Factory class to produce {@link FixedMetadataValue}s associated with an {@link AbstractEcoPlugin}.
     *
     * @param plugin The plugin that this factory creates values for.
     */
    public MetadataValueFactory(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Create an {@link FixedMetadataValue} associated with an {@link AbstractEcoPlugin} with a specified value.
     *
     * @param value The value of meta key.
     * @return The created {@link FixedMetadataValue}.
     */
    public FixedMetadataValue create(@NotNull final Object value) {
        return new FixedMetadataValue(this.getPlugin(), value);
    }
}

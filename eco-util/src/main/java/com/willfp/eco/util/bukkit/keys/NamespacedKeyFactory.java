package com.willfp.eco.util.bukkit.keys;

import com.willfp.eco.util.factory.PluginDependentFactory;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.NamespacedKey;

public class NamespacedKeyFactory extends PluginDependentFactory {
    public NamespacedKeyFactory(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    public NamespacedKey create(String key) {
        return new NamespacedKey(this.plugin, key);
    }
}

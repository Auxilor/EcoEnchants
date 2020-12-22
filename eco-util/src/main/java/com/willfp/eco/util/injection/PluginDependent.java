package com.willfp.eco.util.injection;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;

public abstract class PluginDependent {
    protected final AbstractEcoPlugin plugin;

    protected PluginDependent(AbstractEcoPlugin plugin) {
        this.plugin = plugin;
    }
}

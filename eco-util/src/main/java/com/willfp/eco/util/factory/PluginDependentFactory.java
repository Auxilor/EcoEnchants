package com.willfp.eco.util.factory;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;

public abstract class PluginDependentFactory<T> extends PluginDependent implements AbstractFactory<T> {
    protected PluginDependentFactory(AbstractEcoPlugin plugin) {
        super(plugin);
    }
}

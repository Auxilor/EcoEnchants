package com.willfp.eco.util.factory;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;

public abstract class PluginDependentFactory extends PluginDependent implements AbstractFactory {
    protected PluginDependentFactory(AbstractEcoPlugin plugin) {
        super(plugin);
    }
}

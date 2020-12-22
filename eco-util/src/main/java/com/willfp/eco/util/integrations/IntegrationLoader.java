package com.willfp.eco.util.integrations;

import com.willfp.eco.util.lambda.Callable;

public class IntegrationLoader {
    private final Callable callable;
    private final String pluginName;

    public IntegrationLoader(String pluginName, Callable onLoad) {
        this.callable = onLoad;
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void load() {
        callable.call();
    }
}

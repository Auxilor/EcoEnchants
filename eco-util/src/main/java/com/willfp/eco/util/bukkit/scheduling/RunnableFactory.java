package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.factory.PluginDependentFactory;
import com.willfp.eco.util.lambda.InputCallable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;

public class RunnableFactory extends PluginDependentFactory {
    public RunnableFactory(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    public EcoBukkitRunnable create(InputCallable<EcoBukkitRunnable> callable) {
        return new EcoBukkitRunnable(this.getPlugin()) {
            @Override
            public void run() {
                callable.call(this);
            }
        };
    }
}

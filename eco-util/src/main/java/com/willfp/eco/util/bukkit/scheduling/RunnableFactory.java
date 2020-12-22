package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.factory.PluginDependentFactory;
import com.willfp.eco.util.lambda.InputCallable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.jetbrains.annotations.NotNull;

public class RunnableFactory extends PluginDependentFactory {
    /**
     * Factory class to produce {@link EcoBukkitRunnable}s associated with an {@link AbstractEcoPlugin}.
     *
     * @param plugin The plugin that this factory creates runnables for.
     */
    public RunnableFactory(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Create an {@link EcoBukkitRunnable}.
     *
     * @param callable Lambda of the code to run, where the parameter represents the instance of the runnable.
     * @return The created {@link EcoBukkitRunnable}.
     */
    public EcoBukkitRunnable create(@NotNull final InputCallable<EcoBukkitRunnable> callable) {
        return new EcoBukkitRunnable(this.getPlugin()) {
            @Override
            public void run() {
                callable.call(this);
            }
        };
    }
}

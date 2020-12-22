package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public abstract class EcoBukkitRunnable extends BukkitRunnable {
    private final AbstractEcoPlugin plugin;

    EcoBukkitRunnable(AbstractEcoPlugin plugin) {
        this.plugin = plugin;
    }

    protected AbstractEcoPlugin getPlugin() {
        return this.plugin;
    }

    @NotNull
    public synchronized BukkitTask runTask() {
        return super.runTask(plugin);
    }

    @NotNull
    public synchronized BukkitTask runTaskAsynchronously() {
        return super.runTaskAsynchronously(plugin);
    }

    @NotNull
    public synchronized BukkitTask runTaskLater(long delay) {
        return super.runTaskLater(plugin, delay);
    }

    @NotNull
    public synchronized BukkitTask runTaskLaterAsynchronously(long delay) {
        return super.runTaskLaterAsynchronously(plugin, delay);
    }

    @NotNull
    public synchronized BukkitTask runTaskTimer(long delay, long period) {
        return super.runTaskTimer(plugin, delay, period);
    }

    @NotNull
    public synchronized BukkitTask runTaskTimerAsynchronously(long delay, long period) {
        return super.runTaskTimerAsynchronously(plugin, delay, period);
    }
}

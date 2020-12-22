package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.lambda.Callable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class EcoScheduler extends PluginDependent implements Scheduler {
    public EcoScheduler(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public BukkitTask runLater(Callable callable, long ticksLater) {
        return Bukkit.getServer().getScheduler().runTaskLater(this.plugin, callable::call, ticksLater);
    }

    @Override
    public BukkitTask runTimer(Callable callable, long delay, long repeat) {
        return Bukkit.getServer().getScheduler().runTaskTimer(this.plugin, callable::call, delay, repeat);
    }

    @Override
    public BukkitTask runAsyncTimer(Callable callable, long delay, long repeat) {
        return Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, callable::call, delay, repeat);
    }

    @Override
    public BukkitTask run(Callable callable) {
        return Bukkit.getServer().getScheduler().runTask(this.plugin, callable::call);
    }

    @Override
    public int syncRepeating(Runnable runnable, long delay, long repeat) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, runnable, delay, repeat);
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(this.plugin);
    }
}

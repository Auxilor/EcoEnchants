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
        return Bukkit.getServer().getScheduler().runTaskLater(this.getPlugin(), callable::call, ticksLater);
    }

    @Override
    public BukkitTask runTimer(Callable callable, long delay, long repeat) {
        return Bukkit.getServer().getScheduler().runTaskTimer(this.getPlugin(), callable::call, delay, repeat);
    }

    @Override
    public BukkitTask runAsyncTimer(Callable callable, long delay, long repeat) {
        return Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this.getPlugin(), callable::call, delay, repeat);
    }

    @Override
    public BukkitTask run(Runnable runnable) {
        return Bukkit.getServer().getScheduler().runTask(this.getPlugin(), runnable::run);
    }

    @Override
    public BukkitTask runAsync(Callable callable) {
        return Bukkit.getServer().getScheduler().runTaskAsynchronously(this.getPlugin(), callable::call);
    }

    @Override
    public int syncRepeating(Runnable runnable, long delay, long repeat) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), runnable, delay, repeat);
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(this.getPlugin());
    }
}

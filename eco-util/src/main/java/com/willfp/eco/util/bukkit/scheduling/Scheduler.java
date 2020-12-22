package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.lambda.Callable;
import org.bukkit.scheduler.BukkitTask;

public interface Scheduler {
    BukkitTask runLater(Callable callable, long ticksLater);
    BukkitTask runTimer(Callable callable, long delay, long repeat);
    BukkitTask runAsyncTimer(Callable callable, long delay, long repeat);
    BukkitTask run(Callable callable);
    void cancelAll();
}

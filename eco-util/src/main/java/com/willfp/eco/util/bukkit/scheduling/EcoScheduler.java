package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.lambda.Callable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class EcoScheduler extends PluginDependent implements Scheduler {
    /**
     * Create a scheduler to manage the tasks of an {@link AbstractEcoPlugin}.
     *
     * @param plugin The plugin to manage.
     */
    @ApiStatus.Internal
    public EcoScheduler(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Run the task after a specified tick delay.
     *
     * @param callable   The lambda to run.
     * @param ticksLater The amount of ticks to wait before execution.
     * @return The created {@link BukkitTask}.
     */
    @Override
    public BukkitTask runLater(@NotNull final Callable callable,
                               final long ticksLater) {
        return Bukkit.getScheduler().runTaskLater(this.getPlugin(), callable::call, ticksLater);
    }

    /**
     * Run the task repeatedly on a timer.
     *
     * @param callable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The created {@link BukkitTask}.
     */
    @Override
    public BukkitTask runTimer(@NotNull final Callable callable,
                               final long delay,
                               final long repeat) {
        return Bukkit.getScheduler().runTaskTimer(this.getPlugin(), callable::call, delay, repeat);
    }

    /**
     * Run the task repeatedly and asynchronously on a timer.
     *
     * @param callable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The created {@link BukkitTask}.
     */
    @Override
    public BukkitTask runAsyncTimer(@NotNull final Callable callable,
                                    final long delay,
                                    final long repeat) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(this.getPlugin(), callable::call, delay, repeat);
    }

    /**
     * Run the task.
     *
     * @param runnable The lambda to run.
     * @return The created {@link BukkitTask}.
     */
    @Override
    public BukkitTask run(@NotNull final Runnable runnable) {
        return Bukkit.getScheduler().runTask(this.getPlugin(), runnable);
    }

    /**
     * Run the task asynchronously.
     *
     * @param callable The lambda to run.
     * @return The created {@link BukkitTask}.
     */
    @Override
    public BukkitTask runAsync(@NotNull final Callable callable) {
        return Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), callable::call);
    }

    /**
     * Schedule the task to be ran repeatedly on a timer.
     *
     * @param runnable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The id of the task.
     */
    @Override
    public int syncRepeating(@NotNull final Runnable runnable,
                             final long delay,
                             final long repeat) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), runnable, delay, repeat);
    }

    /**
     * Cancel all running tasks from the linked {@link AbstractEcoPlugin}.
     */
    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(this.getPlugin());
    }
}

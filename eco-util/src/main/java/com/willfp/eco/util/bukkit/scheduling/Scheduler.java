package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.lambda.Callable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public interface Scheduler {
    /**
     * Run the task after a specified tick delay.
     *
     * @param callable   The lambda to run.
     * @param ticksLater The amount of ticks to wait before execution.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask runLater(@NotNull Callable callable, long ticksLater);

    /**
     * Run the task repeatedly on a timer.
     *
     * @param callable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask runTimer(@NotNull Callable callable, long delay, long repeat);

    /**
     * Run the task repeatedly and asynchronously on a timer.
     *
     * @param callable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask runAsyncTimer(@NotNull Callable callable, long delay, long repeat);

    /**
     * Run the task.
     *
     * @param runnable The lambda to run.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask run(@NotNull Runnable runnable);

    /**
     * Run the task asynchronously.
     *
     * @param callable The lambda to run.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask runAsync(@NotNull Callable callable);

    /**
     * Schedule the task to be ran repeatedly on a timer.
     *
     * @param runnable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The id of the task.
     */
    int syncRepeating(@NotNull Runnable runnable, long delay, long repeat);

    /**
     * Cancel all running tasks from the linked {@link AbstractEcoPlugin}.
     */
    void cancelAll();
}

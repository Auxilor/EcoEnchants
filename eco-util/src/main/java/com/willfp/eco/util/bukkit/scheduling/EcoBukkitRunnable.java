package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public abstract class EcoBukkitRunnable extends BukkitRunnable {
    /**
     * The linked {@link AbstractEcoPlugin} to associate runnables with.
     */
    private final AbstractEcoPlugin plugin;

    /**
     * Creates a new {@link EcoBukkitRunnable}.
     * <p>
     * Cannot be instantiated normally, use {@link RunnableFactory}.
     *
     * @param plugin The {@link AbstractEcoPlugin} to associate runnables with.
     */
    @ApiStatus.Internal
    EcoBukkitRunnable(@NotNull final AbstractEcoPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the {@link AbstractEcoPlugin} that created this runnable.
     *
     * @return The linked plugin.
     */
    protected final AbstractEcoPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * Run the task.
     *
     * @return The created {@link BukkitTask}.
     */
    @NotNull
    public final synchronized BukkitTask runTask() {
        return super.runTask(plugin);
    }

    /**
     * Run the task asynchronously.
     *
     * @return The created {@link BukkitTask}
     */
    @NotNull
    public final synchronized BukkitTask runTaskAsynchronously() {
        return super.runTaskAsynchronously(plugin);
    }

    /**
     * Run the task after a specified number of ticks.
     *
     * @param delay The number of ticks to wait.
     * @return The created {@link BukkitTask}
     */
    @NotNull
    public final synchronized BukkitTask runTaskLater(final long delay) {
        return super.runTaskLater(plugin, delay);
    }

    /**
     * Run the task asynchronously after a specified number of ticks.
     *
     * @param delay The number of ticks to wait.
     * @return The created {@link BukkitTask}
     */
    @NotNull
    public final synchronized BukkitTask runTaskLaterAsynchronously(final long delay) {
        return super.runTaskLaterAsynchronously(plugin, delay);
    }

    /**
     * Run the task repeatedly on a timer.
     *
     * @param delay  The delay before the task is first ran (in ticks).
     * @param period The ticks elapsed before the task is ran again.
     * @return The created {@link BukkitTask}
     */
    @NotNull
    public final synchronized BukkitTask runTaskTimer(final long delay, final long period) {
        return super.runTaskTimer(plugin, delay, period);
    }

    /**
     * Run the task repeatedly on a timer asynchronously.
     *
     * @param delay  The delay before the task is first ran (in ticks).
     * @param period The ticks elapsed before the task is ran again.
     * @return The created {@link BukkitTask}
     */
    @NotNull
    public final synchronized BukkitTask runTaskTimerAsynchronously(final long delay, final long period) {
        return super.runTaskTimerAsynchronously(plugin, delay, period);
    }
}

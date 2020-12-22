package com.willfp.eco.util.drops.internal;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FastCollatedDropQueue extends InternalDropQueue {
    /**
     * The {@link CollatedDrops} linked to every player.
     * <p>
     * Cleared and updated every tick.
     */
    private static final HashMap<Player, CollatedDrops> COLLATED_MAP = new HashMap<>();

    /**
     * Backend implementation of {@link AbstractDropQueue}
     * {@link this#push()} adds to a map that creates a new {@link InternalDropQueue} at the end of every tick
     * <p>
     * The drops are not instantly pushed when called, instead the map is iterated over at the end of every tick. This massively improves performance.
     *
     * @param player The player to link the queue with.
     */
    public FastCollatedDropQueue(Player player) {
        super(player);
    }

    /**
     * Queues the drops to be managed by the {@link CollatedRunnable}.
     */
    @Override
    public void push() {
        CollatedDrops fetched = COLLATED_MAP.get(player);
        CollatedDrops collatedDrops = fetched == null ? new CollatedDrops(items, loc, xp) : fetched.addDrops(items).setLocation(loc).addXp(xp);
        COLLATED_MAP.put(player, collatedDrops);
    }

    /**
     * The items, location, and xp linked to a player's drops.
     */
    private static final class CollatedDrops {
        /**
         * A collection of all ItemStacks to be dropped at the end of the tick.
         */
        private final List<ItemStack> drops;

        /**
         * The location to drop the items at.
         */
        private Location location;

        /**
         * The xp to give to the player.
         */
        private int xp;

        private CollatedDrops(@NotNull final List<ItemStack> drops,
                              @NotNull final Location location,
                              final int xp) {
            this.drops = drops;
            this.location = location;
            this.xp = xp;
        }

        /**
         * Get the drops in the queue.
         *
         * @return A {@link List} of the drops to be given.
         */
        @NotNull
        public List<ItemStack> getDrops() {
            return drops;
        }

        /**
         * Get the location to drop the items and spawn the xp.
         *
         * @return The location.
         */
        @NotNull
        public Location getLocation() {
            return location;
        }

        /**
         * Get the experience to give to the player.
         *
         * @return The amount of experience to give.
         */
        public int getXp() {
            return xp;
        }

        /**
         * Add {@link ItemStack}s to the queue.
         *
         * @param toAdd The items to add.
         * @return The instance of the {@link CollatedDrops}.
         */
        public CollatedDrops addDrops(@NotNull final List<ItemStack> toAdd) {
            drops.addAll(toAdd);
            return this;
        }

        /**
         * Set the location of the queue.
         *
         * @param loc The location to set.
         * @return The instance of the {@link CollatedDrops}.
         */
        public CollatedDrops setLocation(@NotNull final Location loc) {
            this.location = loc;
            return this;
        }

        /**
         * Add xp to the queue.
         *
         * @param xp The amount of xp to add.
         * @return The instance of the {@link CollatedDrops}.
         */
        public CollatedDrops addXp(final int xp) {
            this.xp += xp;
            return this;
        }

        @Override
        public String toString() {
            return "CollatedDrops{" +
                    "drops=" + drops +
                    ", location=" + location +
                    ", xp=" + xp +
                    '}';
        }
    }

    public static class CollatedRunnable extends PluginDependent {
        /**
         * The {@link BukkitTask} that the runnable represents.
         */
        private final BukkitTask runnableTask;

        /**
         * Create and run a new runnable to process collated drops.
         *
         * @param plugin The {@link AbstractEcoPlugin} that manages the processing.
         */
        @ApiStatus.Internal
        public CollatedRunnable(@NotNull final AbstractEcoPlugin plugin) {
            super(plugin);
            runnableTask = plugin.getScheduler().runTimer(() -> {
                for (Map.Entry<Player, CollatedDrops> entry : COLLATED_MAP.entrySet()) {
                    new InternalDropQueue(entry.getKey())
                            .setLocation(entry.getValue().getLocation())
                            .addItems(entry.getValue().getDrops())
                            .addXP(entry.getValue().getXp())
                            .push();
                }
                COLLATED_MAP.clear();
            }, 0, 1);
        }

        /**
         * Get the {@link BukkitTask} that the runnable represents.
         *
         * @return The linked {@link BukkitTask}.
         */
        public BukkitTask getRunnableTask() {
            return runnableTask;
        }
    }
}
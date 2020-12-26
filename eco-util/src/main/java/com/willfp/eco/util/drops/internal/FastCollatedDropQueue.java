package com.willfp.eco.util.drops.internal;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
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
    public FastCollatedDropQueue(@NotNull final Player player) {
        super(player);
    }

    /**
     * Queues the drops to be managed by the {@link CollatedRunnable}.
     */
    @Override
    public void push() {
        CollatedDrops fetched = COLLATED_MAP.get(getPlayer());
        CollatedDrops collatedDrops = fetched == null ? new CollatedDrops(getItems(), getLoc(), getXp()) : fetched.addDrops(getItems()).setLocation(getLoc()).addXp(getXp());
        COLLATED_MAP.put(this.getPlayer(), collatedDrops);
    }

    /**
     * The items, location, and xp linked to a player's drops.
     */
    @ToString
    private static final class CollatedDrops {
        /**
         * A collection of all ItemStacks to be dropped at the end of the tick.
         */
        @Getter
        private final List<ItemStack> drops;

        /**
         * The location to drop the items at.
         */
        @Getter
        @Setter
        @Accessors(chain = true)
        private Location location;

        /**
         * The xp to give to the player.
         */
        @Getter
        private int xp;

        private CollatedDrops(@NotNull final List<ItemStack> drops,
                              @NotNull final Location location,
                              final int xp) {
            this.drops = drops;
            this.location = location;
            this.xp = xp;
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
         * Add xp to the queue.
         *
         * @param xp The amount of xp to add.
         * @return The instance of the {@link CollatedDrops}.
         */
        public CollatedDrops addXp(final int xp) {
            this.xp += xp;
            return this;
        }
    }

    public static class CollatedRunnable extends PluginDependent {
        /**
         * The {@link BukkitTask} that the runnable represents.
         */
        @Getter
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
    }
}

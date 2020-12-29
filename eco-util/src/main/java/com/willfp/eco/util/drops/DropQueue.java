package com.willfp.eco.util.drops;

import com.willfp.eco.util.drops.internal.AbstractDropQueue;
import com.willfp.eco.util.drops.internal.DropManager;
import com.willfp.eco.util.drops.internal.DropQueueType;
import com.willfp.eco.util.drops.internal.FastCollatedDropQueue;
import com.willfp.eco.util.drops.internal.InternalDropQueue;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DropQueue {
    /**
     * The internally used {@link AbstractDropQueue}.
     */
    private final AbstractDropQueue handle;

    /**
     * Create {@link DropQueue} linked to player.
     * <p>
     * All drops should be passed through a drop queue for telekinesis integration.
     *
     * @param player The player.
     */
    public DropQueue(@NotNull final Player player) {
        handle = DropManager.getType() == DropQueueType.COLLATED ? new FastCollatedDropQueue(player) : new InternalDropQueue(player);
    }

    /**
     * Add item to queue.
     *
     * @param item The item to add.
     * @return The DropQueue.
     */
    public DropQueue addItem(@NotNull final ItemStack item) {
        handle.addItem(item);
        return this;
    }

    /**
     * Add multiple items to queue.
     *
     * @param itemStacks The items to add.
     * @return The DropQueue.
     */
    public DropQueue addItems(@NotNull final Collection<ItemStack> itemStacks) {
        handle.addItems(itemStacks);
        return this;
    }

    /**
     * Add xp to queue.
     *
     * @param amount The amount to add.
     * @return The DropQueue.
     */
    public DropQueue addXP(final int amount) {
        handle.addXP(amount);
        return this;
    }

    /**
     * Set location of the origin of the drops.
     *
     * @param location The location.
     * @return The DropQueue.
     */
    public DropQueue setLocation(@NotNull final Location location) {
        handle.setLocation(location);
        return this;
    }

    /**
     * Force the queue to act as if player is telekinetic.
     *
     * @return The DropQueue.
     */
    public DropQueue forceTelekinesis() {
        handle.forceTelekinesis();
        return this;
    }

    /**
     * Push the queue.
     */
    public void push() {
        handle.push();
    }
}

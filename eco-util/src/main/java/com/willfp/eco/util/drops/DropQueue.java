package com.willfp.eco.util.drops;

import com.willfp.eco.util.drops.internal.AbstractDropQueue;
import com.willfp.eco.util.drops.internal.DropManager;
import com.willfp.eco.util.drops.internal.DropQueueType;
import com.willfp.eco.util.drops.internal.FastCollatedDropQueue;
import com.willfp.eco.util.drops.internal.InternalDropQueue;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * All drops should be sent through a {@link DropQueue}
 */
public class DropQueue {
    private final AbstractDropQueue handle;

    /**
     * Create {@link DropQueue} linked to player
     *
     * @param player The player
     */
    public DropQueue(Player player) {
        handle = DropManager.getType() == DropQueueType.COLLATED ? new FastCollatedDropQueue(player) : new InternalDropQueue(player);
    }

    /**
     * Add item to queue
     *
     * @param item The item to add
     * @return The DropQueue
     */
    public DropQueue addItem(ItemStack item) {
        handle.addItem(item);
        return this;
    }

    /**
     * Add multiple items to queue
     *
     * @param itemStacks The items to add
     * @return The DropQueue
     */
    public DropQueue addItems(Collection<ItemStack> itemStacks) {
        handle.addItems(itemStacks);
        return this;
    }

    /**
     * Add xp to queue
     *
     * @param amount The amount to add
     * @return The DropQueue
     */
    public DropQueue addXP(int amount) {
        handle.addXP(amount);
        return this;
    }

    /**
     * Set location of the origin of the drops
     *
     * @param location The location
     * @return The DropQueue
     */
    public DropQueue setLocation(Location location) {
        handle.setLocation(location);
        return this;
    }

    /**
     * Force the queue to act as if player is telekinetic
     *
     * @return The DropQueue
     */
    public DropQueue forceTelekinesis() {
        handle.forceTelekinesis();
        return this;
    }

    /**
     * Set the queue to test specific item for telekinesis
     * Default item is the player's held item, however for this is required for Tridents.
     *
     * @param item The item to test
     * @return The DropQueue
     */
    public DropQueue setItem(ItemStack item) {
        handle.setItem(item);
        return this;
    }

    /**
     * Push the queue
     */
    public void push() {
        handle.push();
    }
}
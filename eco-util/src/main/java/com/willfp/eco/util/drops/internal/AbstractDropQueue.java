package com.willfp.eco.util.drops.internal;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface AbstractDropQueue {
    /**
     * Add item to queue
     *
     * @param item The item to add
     * @return The DropQueue
     */
    AbstractDropQueue addItem(ItemStack item);

    /**
     * Add multiple items to queue
     *
     * @param itemStacks The items to add
     * @return The DropQueue
     */
    AbstractDropQueue addItems(Collection<ItemStack> itemStacks);

    /**
     * Add xp to queue
     *
     * @param amount The amount to add
     * @return The DropQueue
     */
    AbstractDropQueue addXP(int amount);

    /**
     * Set location of the origin of the drops
     *
     * @param location The location
     * @return The DropQueue
     */
    AbstractDropQueue setLocation(Location location);

    /**
     * Force the queue to act as if player is telekinetic
     *
     * @return The DropQueue
     */
    AbstractDropQueue forceTelekinesis();

    /**
     * Set the queue to test specific item for telekinesis
     * Default item is the player's held item, however for this is required for Tridents.
     *
     * @param item The item to test
     * @return The DropQueue
     */
    AbstractDropQueue setItem(ItemStack item);

    /**
     * Push the queue
     */
    void push();
}

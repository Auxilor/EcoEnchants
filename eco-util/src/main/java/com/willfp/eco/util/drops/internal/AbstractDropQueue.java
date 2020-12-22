package com.willfp.eco.util.drops.internal;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface AbstractDropQueue {
    /**
     * Add item to queue.
     *
     * @param item The item to add.
     * @return The DropQueue.
     */
    AbstractDropQueue addItem(@NotNull ItemStack item);

    /**
     * Add multiple items to queue
     *
     * @param itemStacks The items to add.
     * @return The DropQueue.
     */
    AbstractDropQueue addItems(@NotNull Collection<ItemStack> itemStacks);

    /**
     * Add xp to queue.
     *
     * @param amount The amount to add.
     * @return The DropQueue.
     */
    AbstractDropQueue addXP(int amount);

    /**
     * Set location of the origin of the drops.
     *
     * @param location The location.
     * @return The DropQueue.
     */
    AbstractDropQueue setLocation(@NotNull Location location);

    /**
     * Force the queue to act as if player is telekinetic.
     *
     * @return The DropQueue.
     */
    AbstractDropQueue forceTelekinesis();

    /**
     * Push the queue.
     */
    void push();
}

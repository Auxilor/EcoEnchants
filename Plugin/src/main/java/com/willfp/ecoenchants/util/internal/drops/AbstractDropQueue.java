package com.willfp.ecoenchants.util.internal.drops;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface AbstractDropQueue {
    /**
     * Add item to queue
     *
     * @param item The item to add
     *
     * @return The DropQueue
     */
    AbstractDropQueue addItem(ItemStack item);

    /**
     * Add multiple items to queue
     *
     * @param itemStacks The items to add
     *
     * @return The DropQueue
     */
    AbstractDropQueue addItems(Collection<ItemStack> itemStacks);

    /**
     * Add xp to queue
     *
     * @param amount The amount to add
     *
     * @return The DropQueue
     */
    AbstractDropQueue addXP(int amount);

    /**
     * Set location of the origin of the drops
     *
     * @param l The location
     *
     * @return The DropQueue
     */
    AbstractDropQueue setLocation(Location l);

    /**
     * Force the queue to act as if player has {@link com.willfp.ecoenchants.enchantments.EcoEnchants#TELEKINESIS}
     *
     * @return The DropQueue
     */
    AbstractDropQueue forceTelekinesis();

    /**
     * Set the queue to test specific item for {@link com.willfp.ecoenchants.enchantments.EcoEnchants#TELEKINESIS}
     * Default item is the player's held item, however for this is required for Tridents.
     *
     * @param item The item to test
     *
     * @return The DropQueue
     */
    AbstractDropQueue setItem(ItemStack item);

    /**
     * Push the queue
     */
    void push();
}

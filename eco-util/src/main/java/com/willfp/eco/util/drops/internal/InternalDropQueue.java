package com.willfp.eco.util.drops.internal;

import com.willfp.eco.util.drops.telekinesis.TelekinesisUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InternalDropQueue implements AbstractDropQueue {
    /**
     * The items that the DropQueue stores.
     */
    @Getter(AccessLevel.PROTECTED)
    private final List<ItemStack> items;

    /**
     * The experience to give.
     */
    @Getter(AccessLevel.PROTECTED)
    private int xp;

    /**
     * The owner of the queue.
     */
    @Getter(AccessLevel.PROTECTED)
    private final Player player;

    /**
     * The location to drop the items and xp.
     */
    @Getter(AccessLevel.PROTECTED)
    private Location loc;

    /**
     * If the queue should be processed telekinetically.
     */
    @Getter(AccessLevel.PROTECTED)
    private boolean hasTelekinesis = false;

    /**
     * Create a DropQueue linked to player.
     *
     * @param player The player.
     */
    public InternalDropQueue(@NotNull final Player player) {
        this.items = new ArrayList<>();
        this.xp = 0;
        this.player = player;
        this.loc = player.getLocation();
    }

    /**
     * Add item to queue.
     *
     * @param item The item to add.
     * @return The DropQueue.
     */
    @Override
    public AbstractDropQueue addItem(@NotNull final ItemStack item) {
        this.items.add(item);
        return this;
    }

    /**
     * Add multiple items to queue.
     *
     * @param itemStacks The items to add.
     * @return The DropQueue.
     */
    @Override
    public AbstractDropQueue addItems(@NotNull final Collection<ItemStack> itemStacks) {
        this.items.addAll(itemStacks);
        return this;
    }

    /**
     * Add xp to queue.
     *
     * @param amount The amount to add.
     * @return The DropQueue.
     */
    @Override
    public AbstractDropQueue addXP(final int amount) {
        this.xp += amount;
        return this;
    }

    /**
     * Set location of the origin of the drops.
     *
     * @param location The location.
     * @return The DropQueue.
     */
    @Override
    public AbstractDropQueue setLocation(@NotNull final Location location) {
        this.loc = location;
        return this;
    }

    /**
     * Force the queue to act as if player has a telekinetic item.
     *
     * @return The DropQueue.
     */
    @Override
    public AbstractDropQueue forceTelekinesis() {
        this.hasTelekinesis = true;
        return this;
    }

    /**
     * Push the queue.
     */
    public void push() {
        if (!hasTelekinesis) {
            hasTelekinesis = TelekinesisUtils.testPlayer(player);
        }

        World world = loc.getWorld();
        assert world != null;
        loc = loc.add(0.5, 0.5, 0.5);

        if (hasTelekinesis) {
            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(items.toArray(new ItemStack[0]));
            for (ItemStack drop : leftover.values()) {
                world.dropItem(loc, drop).setVelocity(new Vector());
            }
            if (xp > 0) {
                PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, xp);
                Bukkit.getPluginManager().callEvent(event);
                ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(player.getLocation().add(0, 0.2, 0), EntityType.EXPERIENCE_ORB);
                orb.setVelocity(new Vector(0, 0, 0));
                orb.setExperience(event.getAmount());
            }
        } else {
            for (ItemStack drop : items) {
                world.dropItem(loc, drop).setVelocity(new Vector());
            }
            if (xp > 0) {
                ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(loc, EntityType.EXPERIENCE_ORB);
                orb.setExperience(xp);
            }
        }
    }
}

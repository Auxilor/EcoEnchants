package com.willfp.eco.util.drops.internal;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InternalDropQueue implements AbstractDropQueue {
    protected final List<ItemStack> items;
    protected int xp;
    protected final Player player;
    protected Location loc;
    protected boolean hasTelekinesis = false;
    protected ItemStack item;

    /**
     * Create a DropQueue linked to player
     *
     * @param player The player
     */
    public InternalDropQueue(Player player) {
        this.items = new ArrayList<>();
        this.xp = 0;
        this.player = player;
        this.loc = player.getLocation();
        this.item = player.getInventory().getItemInMainHand();
    }

    /**
     * Add item to queue
     *
     * @param item The item to add
     *
     * @return The DropQueue
     */
    @Override
    public AbstractDropQueue addItem(ItemStack item) {
        this.items.add(item);
        return this;
    }

    /**
     * Add multiple items to queue
     *
     * @param itemStacks The items to add
     *
     * @return The DropQueue
     */
    @Override
    public AbstractDropQueue addItems(Collection<ItemStack> itemStacks) {
        this.items.addAll(itemStacks);
        return this;
    }

    /**
     * Add xp to queue
     *
     * @param amount The amount to add
     *
     * @return The DropQueue
     */
    @Override
    public AbstractDropQueue addXP(int amount) {
        this.xp += amount;
        return this;
    }

    /**
     * Set location of the origin of the drops
     *
     * @param l The location
     *
     * @return The DropQueue
     */
    @Override
    public AbstractDropQueue setLocation(Location l) {
        this.loc = l;
        return this;
    }

    /**
     * Force the queue to act as if player has a telekinetic item
     *
     * @return The DropQueue
     */
    @Override
    public AbstractDropQueue forceTelekinesis() {
        this.hasTelekinesis = true;
        return this;
    }

    /**
     * Set the queue to test specific item for telekinesis
     * Default item is the player's held item, however for this is required for Tridents.
     *
     * @param item The item to test
     *
     * @return The DropQueue
     */
    @Override
    public AbstractDropQueue setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    /**
     * Push the queue
     */
    public void push() {
        if(!hasTelekinesis) hasTelekinesis = AbstractEcoPlugin.getInstance().getTelekineticTests().testPlayer(player);

        World world = loc.getWorld();
        assert world != null;
        loc = loc.add(0.5, 0.5, 0.5);

        if(hasTelekinesis) {
            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(items.toArray(new ItemStack[0]));
            for(ItemStack drop : leftover.values()) {
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

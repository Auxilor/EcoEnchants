package com.willfp.ecoenchants.events.entitydeathbyentity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Event triggered when entity is killed by entity.
 */
public class EntityDeathByEntityEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The {@link LivingEntity} killed
     */
    private final LivingEntity victim;

    /**
     * The {@link Entity} that killed;
     */
    private final Entity damager;

    /**
     * The associated {@link EntityDeathEvent}
     */
    private final EntityDeathEvent deathEvent;

    /**
     * The entity drops
     */
    private final List<ItemStack> drops;

    /**
     * The xp to drop
     */
    private final int xp;

    /**
     * Create event based off parameters
     * @param victim The killed entity
     * @param damager The killer
     * @param drops The item drops
     * @param xp The amount of xp to drop
     * @param deathEvent The associated {@link EntityDeathEvent}
     */
    public EntityDeathByEntityEvent(@NotNull LivingEntity victim, @NotNull Entity damager, @NotNull List<ItemStack> drops, int xp, @NotNull EntityDeathEvent deathEvent) {
        this.victim = victim;
        this.damager = damager;
        this.drops = drops;
        this.xp = xp;
        this.deathEvent = deathEvent;
    }

    /**
     * Get victim
     * @return The victim
     */
    public LivingEntity getVictim() {
        return this.victim;
    }

    /**
     * Get killer
     * @return The killer
     */
    public Entity getKiller() {
        return this.damager;
    }

    /**
     * Get xp amount
     * @return The xp
     */
    public int getDroppedExp() {
        return this.xp;
    }

    /**
     * Get drops
     * @return {@link List} of drops
     */
    public List<ItemStack> getDrops() {
        return this.drops;
    }

    /**
     * Get associated {@link EntityDeathEvent}
     * Use this to modify event parameters.
     * @return The associated {@link EntityDeathEvent}
     */
    public EntityDeathEvent getDeathEvent() {
        return this.deathEvent;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

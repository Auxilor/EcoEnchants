package com.willfp.eco.util.events.entitydeathbyentity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

class EntityDeathByEntityBuilder {
    private LivingEntity victim = null;
    private Entity damager;
    private EntityDeathEvent deathEvent;

    private List<ItemStack> drops;
    private int xp = 0;
    private boolean dropItems;

    public EntityDeathByEntityBuilder() {

    }

    public LivingEntity getVictim() {
        return this.victim;
    }

    public void setDeathEvent(EntityDeathEvent deathEvent) {
        this.deathEvent = deathEvent;
    }

    public void setVictim(LivingEntity victim) {
        this.victim = victim;
    }

    public void setDamager(Entity damager) {
        this.damager = damager;
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void push() {
        if (this.victim == null) return;
        if (this.damager == null) return;
        if (this.drops == null) return;
        if (this.deathEvent == null) return;

        EntityDeathByEntityEvent event = new EntityDeathByEntityEvent(victim, damager, drops, xp, deathEvent);

        Bukkit.getPluginManager().callEvent(event);
    }
}

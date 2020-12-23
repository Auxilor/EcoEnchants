package com.willfp.eco.util.events.entitydeathbyentity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

class EntityDeathByEntityBuilder {
    /**
     * The killed {@link LivingEntity}.
     */
    @Getter
    @Setter
    private LivingEntity victim = null;

    /**
     * The killer.
     */
    @Getter
    @Setter
    private Entity damager;

    /**
     * The associated {@link EntityDeathEvent}.
     */
    @Getter
    @Setter
    private EntityDeathEvent deathEvent;

    /**
     * The drops to create.
     */
    @Getter
    @Setter
    private List<ItemStack> drops;

    /**
     * The experience to drop.
     */
    @Getter
    @Setter
    private int xp = 0;

    public void push() {
        Validate.notNull(victim);
        Validate.notNull(damager);
        Validate.notNull(drops);
        Validate.notNull(deathEvent);

        EntityDeathByEntityEvent event = new EntityDeathByEntityEvent(victim, damager, drops, xp, deathEvent);

        Bukkit.getPluginManager().callEvent(event);
    }
}

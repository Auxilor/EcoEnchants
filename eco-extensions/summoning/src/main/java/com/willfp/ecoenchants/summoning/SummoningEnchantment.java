package com.willfp.ecoenchants.summoning;

import com.willfp.eco.core.Prerequisite;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.enchantments.util.WeakMetadata;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.NotNull;

public abstract class SummoningEnchantment extends EcoEnchant {
    private final SummoningType summoningType;

    protected SummoningEnchantment(@NotNull final String key,
                                   @NotNull final EnchantmentType type,
                                   @NotNull final SummoningType summoningType,
                                   @NotNull final Prerequisite... prerequisites) {
        super(key, type, prerequisites);

        this.summoningType = summoningType;
    }

    public abstract EntityType getEntity();

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!summoningType.equals(SummoningType.MELEE)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK
            && this.getConfig().getBool("config.ignore-sweep-attacks")) {
            return;
        }

        doSpawn(attacker, victim, level);
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!summoningType.equals(SummoningType.RANGED)) {
            return;
        }

        doSpawn(attacker, victim, level);
    }

    @Override
    public void onTridentDamage(@NotNull final LivingEntity attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                final int level,
                                @NotNull final EntityDamageByEntityEvent event) {
        if (!summoningType.equals(SummoningType.TRIDENT)) {
            return;
        }

        doSpawn(attacker, victim, level);
    }

    private void doSpawn(@NotNull final LivingEntity attacker,
                         @NotNull final LivingEntity victim,
                         final int level) {
        if (summoningType.equals(SummoningType.MELEE)) {
            if (EnchantmentUtils.isFullyChargeIfRequired(this, attacker)) {
                return;
            }
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (WeakMetadata.SUMMONED_ENTITY_MEMORY.containsKey(victim) || WeakMetadata.SUMMONED_ENTITY_TARGET.containsKey(victim)) {
            return;
        }

        Location location = victim.getLocation().clone();
        World world = victim.getWorld();

        int toSpawn = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "mobs-per-level") * level;
        int ticksToLive = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-to-live-per-level") * level;
        double health = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-level") * level;

        for (int i = 0; i < toSpawn; i++) {
            Location locToSpawn = location.clone().add(NumberUtils.randFloat(-3, 3), NumberUtils.randFloat(0, 3), NumberUtils.randFloat(-3, 3));
            Mob entity = (Mob) world.spawnEntity(locToSpawn, this.getEntity());

            entity.setTarget(victim);
            if (health > entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            }
            entity.setHealth(health);
            WeakMetadata.SUMMONED_ENTITY_MEMORY.put(entity, victim);
            WeakMetadata.SUMMONED_ENTITY_TARGET.put(victim, null);
            this.getPlugin().getScheduler().runLater(entity::remove, ticksToLive);
        }
    }

    @EventHandler
    public void onSwitchTarget(@NotNull final EntityTargetEvent event) {
        if (!WeakMetadata.SUMMONED_ENTITY_MEMORY.containsKey(event.getEntity())) {
            return;
        }

        LivingEntity target = (LivingEntity) WeakMetadata.SUMMONED_ENTITY_MEMORY.get(event.getEntity());
        event.setTarget(target);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDropItem(@NotNull final EntityDeathEvent event) {
        if (!WeakMetadata.SUMMONED_ENTITY_MEMORY.containsKey(event.getEntity())) {
            return;
        }

        event.getDrops().clear();
        event.setDroppedExp(0);
    }
}

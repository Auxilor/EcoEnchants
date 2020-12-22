package com.willfp.ecoenchants.summoning;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.FixedMetadataValue;

public abstract class SummoningEnchantment extends EcoEnchant {
    private final SummoningType summoningType;

    protected SummoningEnchantment(String key, EnchantmentType type, SummoningType summoningType, Prerequisite... prerequisites) {
        super(key, type, prerequisites);

        this.summoningType = summoningType;
    }

    public abstract EntityType getEntity();

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(!summoningType.equals(SummoningType.MELEE)) return;

        doSpawn(attacker, victim, level);
    }

    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        if(!summoningType.equals(SummoningType.RANGED)) return;

        doSpawn(attacker, victim, level);
    }

    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        if(!summoningType.equals(SummoningType.TRIDENT)) return;

        doSpawn(attacker, victim, level);
    }

    private void doSpawn(LivingEntity attacker, LivingEntity victim, int level) {

        if(summoningType.equals(SummoningType.MELEE) && attacker instanceof Player && ProxyUtils.getProxy(CooldownProxy.class).getAttackCooldown((Player) attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged")) {
            return;
        }

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        if(!victim.getMetadata("eco-target").isEmpty()) return;

        Location location = victim.getLocation().clone();
        World world = victim.getWorld();

        int toSpawn = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "mobs-per-level") * level;
        int ticksToLive = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-to-live-per-level") * level;
        double health = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-level") * level;

        for(int i = 0; i < toSpawn; i++) {
            Location locToSpawn = location.clone().add(NumberUtils.randFloat(-3, 3), NumberUtils.randFloat(0, 3), NumberUtils.randFloat(-3, 3));
            Mob entity = (Mob) world.spawnEntity(locToSpawn, this.getEntity());

            entity.setTarget(victim);
            if(health > entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            entity.setHealth(health);
            entity.setMetadata("eco-target", new FixedMetadataValue(this.getPlugin(), victim));

            this.getPlugin().getScheduler().runLater(entity::remove, ticksToLive);
        }
    }

    @EventHandler
    public void onSwitchTarget(EntityTargetEvent event) {
        if(event.getEntity().getMetadata("eco-target").isEmpty()) return;

        LivingEntity target = (LivingEntity) event.getEntity().getMetadata("eco-target").get(0).value();
        event.setTarget(target);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDropItem(EntityDeathEvent event) {
        if(event.getEntity().getMetadata("eco-target").isEmpty()) return;

        event.getDrops().clear();
        event.setDroppedExp(0);
    }
}

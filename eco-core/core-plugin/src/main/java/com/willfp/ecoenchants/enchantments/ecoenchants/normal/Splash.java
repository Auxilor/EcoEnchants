package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class Splash extends EcoEnchant {
    public Splash() {
        super(
                "splash", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onTridentHit(@NotNull final LivingEntity shooter,
                             final int level,
                             @NotNull final ProjectileHitEvent event) {
        Trident trident = (Trident) event.getEntity();

        ItemStack item = trident.getItem();

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(shooter.getWorld())) {
            return;
        }

        double radius = level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-multiplier");
        double damage = level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");

        for (Entity victim : trident.getNearbyEntities(radius, radius, radius)) {
            if (victim.hasMetadata("NPC")) {
                continue;
            }

            if (!(victim instanceof LivingEntity entity)) {
                continue;
            }

            if (victim.equals(shooter)) {
                continue;
            }

            Bukkit.getPluginManager().callEvent(new EntityDamageByEntityEvent(trident, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));

            if (shooter instanceof Player) {
                if (!AntigriefManager.canInjure((Player) shooter, entity)) {
                    continue;
                }
            }

            entity.damage(damage, trident);
        }
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class Radiance extends EcoEnchant {
    public Radiance() {
        super(
                new EcoEnchantBuilder("radiance", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        double radius = level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-multiplier");
        int duration = level * this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "duration-per-level");

        for (Entity e : arrow.getNearbyEntities(radius, radius, radius)) {
            if(e.hasMetadata("NPC")) continue;

            if (!(e instanceof LivingEntity)) continue;
            LivingEntity entity = (LivingEntity) e;
            if(e.equals(attacker)) continue;

            entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, duration, 0, false, false, false));
        }
    }
}
package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Radiance extends EcoEnchant {
    public Radiance() {
        super(
                "radiance", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Arrow arrow, int level, @NotNull EntityDamageByEntityEvent event) {
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
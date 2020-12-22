package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.google.common.collect.ImmutableSet;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Set;
public class EnderSlayer extends EcoEnchant {
    public EnderSlayer() {
        super(
                "ender_slayer", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    private static final Set<EntityType> endMobs = new ImmutableSet.Builder<EntityType>()
            .add(EntityType.ENDERMITE)
            .add(EntityType.ENDERMAN)
            .add(EntityType.ENDER_DRAGON)
            .add(EntityType.SHULKER)
            .build();


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (!endMobs.contains(victim.getType()))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");

        event.setDamage(event.getDamage() + (level * multiplier));
    }
}

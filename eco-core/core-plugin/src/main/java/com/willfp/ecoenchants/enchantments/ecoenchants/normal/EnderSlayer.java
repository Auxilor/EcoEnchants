package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.google.common.collect.ImmutableSet;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EnderSlayer extends EcoEnchant {
    private static final Set<EntityType> END_MOBS = new ImmutableSet.Builder<EntityType>()
            .add(EntityType.ENDERMITE)
            .add(EntityType.ENDERMAN)
            .add(EntityType.ENDER_DRAGON)
            .add(EntityType.SHULKER)
            .build();

    public EnderSlayer() {
        super(
                "ender_slayer", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!END_MOBS.contains(victim.getType())) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");

        event.setDamage(event.getDamage() + (level * multiplier));
    }
}

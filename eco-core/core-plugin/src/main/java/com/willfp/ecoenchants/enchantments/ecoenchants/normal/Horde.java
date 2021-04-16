package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Horde extends EcoEnchant {
    public Horde() {
        super(
                "horde", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        double distance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level") * level;

        int entitiesNearby = (int) attacker.getNearbyEntities(distance, distance, distance).stream().filter(entity -> entity instanceof LivingEntity).count();

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-per-level");
        multiplier = (1 + (level * multiplier * entitiesNearby));

        event.setDamage(event.getDamage() * multiplier);
    }
}

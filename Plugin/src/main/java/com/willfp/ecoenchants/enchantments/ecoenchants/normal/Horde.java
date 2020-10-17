package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class Horde extends EcoEnchant {
    public Horde() {
        super(
                "horde", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        double distance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level") * level;

        int entitiesNearby = (int) attacker.getNearbyEntities(distance, distance, distance).stream().filter(entity -> entity instanceof LivingEntity).count();

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-per-level");
        multiplier = (1 + (level * multiplier * entitiesNearby));

        event.setDamage(event.getDamage() * multiplier);
    }
}

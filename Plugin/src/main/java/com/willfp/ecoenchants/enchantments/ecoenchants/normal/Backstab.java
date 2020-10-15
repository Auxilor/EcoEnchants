package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public final class Backstab extends EcoEnchant {
    public Backstab() {
        super(
                new EcoEnchantBuilder("backstab", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        Vector pDir = attacker.getLocation().getDirection();
        Vector eDir = victim.getLocation().getDirection();
        double xv = pDir.getX() * eDir.getZ() - pDir.getZ() * eDir.getX();
        double zv = pDir.getX() * eDir.getX() + pDir.getZ() * eDir.getZ();
        double angle = Math.atan2(xv, zv); // Value between -π and +π
        double angleInDegrees = (angle * 180) / Math.PI;

        if(angleInDegrees > 60 || angleInDegrees < -32)
            return;

        event.setDamage(event.getDamage() * ((level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level")) + 1));
    }
}

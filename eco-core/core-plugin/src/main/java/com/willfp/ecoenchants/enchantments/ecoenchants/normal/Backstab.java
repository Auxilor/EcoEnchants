package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Backstab extends EcoEnchant {
    public Backstab() {
        super(
                "backstab", EnchantmentType.NORMAL
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

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = 1 + (multiplier * level);
        event.setDamage(damage * bonus);
    }
}

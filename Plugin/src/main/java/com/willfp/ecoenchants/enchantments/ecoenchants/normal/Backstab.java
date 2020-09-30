package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Backstab extends EcoEnchant {
    public Backstab() {
        super(
                new EcoEnchantBuilder("backstab", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        Vector victimLoc = victim.getLocation().toVector();
        Vector attackerLoc = attacker.getLocation().toVector();
        Vector locationDelta = victimLoc.subtract(attackerLoc);
        Vector attackerDirection = attacker.getLocation().getDirection();
        double dot = locationDelta.dot(attackerDirection);

        if(dot > 0) return;

        event.setDamage(event.getDamage() * ((level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level")) + 1));
    }
}

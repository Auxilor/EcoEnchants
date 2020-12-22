package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
public class Annihilate extends EcoEnchant {
    public Annihilate() {
        super(
                "annihilate", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        double baseMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-multiplier");
        Vector vector = attacker.getLocation().toVector().clone().subtract(victim.getLocation().toVector()).normalize().multiply(level * baseMultiplier).multiply(-1);
        if(!VectorUtils.isFinite(vector)) return;
        vector.setY(0.2);
        if(!VectorUtils.isFinite(vector)) return;
        victim.setVelocity(vector);
    }
}

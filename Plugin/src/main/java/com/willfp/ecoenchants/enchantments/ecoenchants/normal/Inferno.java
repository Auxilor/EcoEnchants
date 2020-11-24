package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
public class Inferno extends EcoEnchant {
    public Inferno() {
        super(
                "inferno", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentLaunch(LivingEntity shooter, Trident trident, int level, ProjectileLaunchEvent event) {
        trident.setFireTicks(Integer.MAX_VALUE);
    }

    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        if(trident.getFireTicks() <= 0) return;

        victim.setFireTicks(100);
    }
}

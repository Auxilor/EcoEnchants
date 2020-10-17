package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
public final class Freerunner extends EcoEnchant {
    public Freerunner() {
        super(
                "freerunner", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onFallDamage(LivingEntity faller, int level, EntityDamageEvent event) {

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        event.setCancelled(true);
    }
}

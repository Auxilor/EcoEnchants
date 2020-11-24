package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
public class Extinguishing extends EcoEnchant {
    public Extinguishing() {
        super(
                "extinguishing", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK))
            return;


        if(!EnchantmentUtils.passedChance(this, level))
            return;

        victim.setFireTicks(0);
    }
}

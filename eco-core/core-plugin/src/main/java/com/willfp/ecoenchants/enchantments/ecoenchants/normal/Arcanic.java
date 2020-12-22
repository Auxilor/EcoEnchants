package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
public class Arcanic extends EcoEnchant {
    public Arcanic() {
        super(
                "arcanic", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if (!(event.getCause().equals(EntityDamageEvent.DamageCause.POISON) || event.getCause().equals(EntityDamageEvent.DamageCause.WITHER)))
            return;


        if(!EnchantmentUtils.passedChance(this, level))
            return;

        event.setCancelled(true);
    }
}

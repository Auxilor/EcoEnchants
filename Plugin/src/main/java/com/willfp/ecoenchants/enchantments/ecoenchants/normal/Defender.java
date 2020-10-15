package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class Defender extends EcoEnchant {
    public Defender() {
        super(
                new EcoEnchantBuilder("defender", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        if(!(victim instanceof Tameable)) return;

        Tameable pet = (Tameable) victim;

        if(pet.getOwner() == null) return;
        if(!pet.getOwner().equals(attacker)) return;

        event.setCancelled(true);
    }
}

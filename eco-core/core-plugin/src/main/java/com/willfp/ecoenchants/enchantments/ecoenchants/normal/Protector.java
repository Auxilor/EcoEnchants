package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Protector extends EcoEnchant {
    public Protector() {
        super(
                "protector", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity uncastVictim, int level, EntityDamageByEntityEvent event) {
        if(!(uncastVictim instanceof Tameable)) return;

        Tameable victim = (Tameable) uncastVictim;
        if(victim.getOwner() == null) return;
        if(!victim.getOwner().equals(attacker)) return;

        event.setCancelled(true);
    }
}

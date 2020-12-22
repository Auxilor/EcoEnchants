package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
public class Evasion extends EcoEnchant {
    public Evasion() {
        super(
                "evasion", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        event.setCancelled(true);
    }
}

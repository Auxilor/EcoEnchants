package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HarmlessnessCurse extends EcoEnchant {
    public HarmlessnessCurse() {
        super(
                "harmlessness_curse", EnchantmentType.CURSE
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (!EnchantmentUtils.passedChance(this, level))
            return;

        event.setDamage(0);
        event.setCancelled(true);
    }
}

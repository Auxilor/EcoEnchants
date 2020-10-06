package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class HarmlessnessCurse extends EcoEnchant {
    public HarmlessnessCurse() {
        super(
                new EcoEnchantBuilder("harmlessness_curse", EnchantmentType.CURSE,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        event.setDamage(0);
        event.setCancelled(true);
    }
}

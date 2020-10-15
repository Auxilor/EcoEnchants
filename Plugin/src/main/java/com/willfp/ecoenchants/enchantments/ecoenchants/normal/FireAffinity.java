package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class FireAffinity extends EcoEnchant {
    public FireAffinity() {
        super(
                new EcoEnchantBuilder("fire_affinity", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(attacker.getFireTicks() == 0) return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");

        double finalMultiplier = (multiplier/100 * level) + 1;

        event.setDamage(event.getDamage() * finalMultiplier);
    }
}

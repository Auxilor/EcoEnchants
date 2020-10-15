package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class Proximity extends EcoEnchant {
    public Proximity() {
        super(
                new EcoEnchantBuilder("proximity", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        double distance = attacker.getLocation().distance(victim.getLocation());

        double decreaseAfter = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "when-closer-than-blocks");

        if(distance <= decreaseAfter) {
            double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");
            double finalMultiplier = (multiplier/100 * level) +1;
            event.setDamage(event.getDamage() * finalMultiplier);
        }
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
public final class Preservation extends EcoEnchant {
    public Preservation() {
        super(
                "preservation", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-less-per-level");

        double multiplier = 1 - (reduction/100 * level);

        event.setDamage(event.getDamage() * multiplier);
    }
}

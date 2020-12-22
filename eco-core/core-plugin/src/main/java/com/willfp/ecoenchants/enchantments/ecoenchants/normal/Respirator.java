package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

public class Respirator extends EcoEnchant {
    public Respirator() {
        super(
                "respirator", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)) return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-less-per-level");

        double multiplier = 1 - (reduction/100 * level);

        event.setDamage(event.getDamage() * multiplier);
    }
}

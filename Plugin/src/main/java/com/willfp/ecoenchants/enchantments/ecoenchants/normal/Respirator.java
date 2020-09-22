package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class Respirator extends EcoEnchant {
    public Respirator() {
        super(
                new EcoEnchantBuilder("respirator", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHurt(EntityDamageEvent event) {
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)) return;

        if(!(event.getEntity() instanceof LivingEntity)) return;

        if(!EnchantChecks.helmet((LivingEntity) event.getEntity(), this)) return;

        int level = EnchantChecks.getHelmetLevel((LivingEntity) event.getEntity(), this);

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-less-per-level");

        double multiplier = 1 - (reduction/100 * level);

        event.setDamage(event.getDamage() * multiplier);
    }
}

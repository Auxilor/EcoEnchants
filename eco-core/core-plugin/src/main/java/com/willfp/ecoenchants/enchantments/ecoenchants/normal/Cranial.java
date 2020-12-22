package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Cranial extends EcoEnchant {
    public Cranial() {
        super(
                "cranial", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        if (!(trident.getLocation().getY() >= victim.getLocation().getY() + victim.getEyeHeight() - 0.22)) return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        double damageMultiplier = (level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}
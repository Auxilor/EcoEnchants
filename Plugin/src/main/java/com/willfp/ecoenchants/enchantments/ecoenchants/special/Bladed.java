package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Bladed extends EcoEnchant {
    public Bladed() {
        super(
                new EcoEnchantBuilder("bladed", EnchantmentType.SPECIAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        double baseDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-base");
        double perLevelDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");

        double totalDamagePercent = (100 + baseDamage + (perLevelDamage * level))/100;

        event.setDamage(event.getDamage() * totalDamagePercent);
    }
}

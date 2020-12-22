package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Optics extends EcoEnchant {
    public Optics() {
        super(
                "optics", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        Location land = arrow.getLocation();
        Location source = attacker.getLocation();

        double distance = land.distance(source);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "block-multiplier");

        double damageMultiplier = (distance * level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}
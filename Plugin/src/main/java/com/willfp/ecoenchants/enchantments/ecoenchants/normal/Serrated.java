package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class Serrated extends EcoEnchant {
    public Serrated() {
        super(
                "serrated", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = 1 + (multiplier * level);
        event.setDamage(damage * bonus);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Butchering extends EcoEnchant {
    public Butchering() {
        super(
                "butchering", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(victim instanceof Monster) return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");
        event.setDamage(event.getDamage() + (level * multiplier));
    }
}

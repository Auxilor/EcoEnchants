package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class Stab extends EcoEnchant {
    public Stab() {
        super(
                new EcoEnchantBuilder("stab", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        double baseDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-base");
        double perLevelDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
        double damage = baseDamage + (level * perLevelDamage);

        event.setDamage(event.getDamage() + damage);
    }
}

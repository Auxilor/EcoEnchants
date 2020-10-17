package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class Goliath extends EcoEnchant {
    public Goliath() {
        super(
                "goliath", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (victim.getHealth() <= attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
            return;

        double timesMoreHealth = victim.getHealth() / attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = 1 + (multiplier * level * timesMoreHealth);
        event.setDamage(damage * bonus);
    }
}

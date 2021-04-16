package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Goliath extends EcoEnchant {
    public Goliath() {
        super(
                "goliath", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (victim.getHealth() <= attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            return;
        }

        double timesMoreHealth = victim.getHealth() / attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = 1 + (multiplier * level * timesMoreHealth);
        if (bonus - 1 > this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-cap")) {
            bonus = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-cap") + 1;
        }
        event.setDamage(damage * bonus);
    }
}

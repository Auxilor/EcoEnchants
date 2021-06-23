package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Parasitic extends EcoEnchant {
    public Parasitic() {
        super(
                "parasitic", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-level");
        double amountToHeal = level * multiplier;
        double newHealth = attacker.getHealth() + amountToHeal;
        if (newHealth > attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            newHealth = attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }

        if (attacker.getHealth() > 0) {
            attacker.setHealth(newHealth);
        }
    }
}

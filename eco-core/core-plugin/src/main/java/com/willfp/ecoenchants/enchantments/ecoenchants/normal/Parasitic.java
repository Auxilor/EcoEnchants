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

    // START OF LISTENERS


    @Override
    public void onArrowDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Arrow arrow, int level, @NotNull EntityDamageByEntityEvent event) {
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-level");
        double amountToHeal = level * multiplier;
        double newHealth = attacker.getHealth() + amountToHeal;
        if (newHealth > attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            newHealth = attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }
        attacker.setHealth(newHealth);
    }
}

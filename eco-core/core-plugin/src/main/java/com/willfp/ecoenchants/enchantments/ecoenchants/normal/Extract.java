package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Extract extends EcoEnchant {
    public Extract() {
        super(
                "extract", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onTridentDamage(@NotNull final LivingEntity attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                final int level,
                                @NotNull final EntityDamageByEntityEvent event) {
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-level");
        double amountToHeal = level * multiplier;
        double newHealth = attacker.getHealth() + amountToHeal;
        if (newHealth > attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            newHealth = attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }
        attacker.setHealth(newHealth);
    }
}

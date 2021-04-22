package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Sycophant extends EcoEnchant {
    public Sycophant() {
        super(
                "sycophant", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onDeflect(@NotNull final Player blocker,
                          @NotNull final LivingEntity attacker,
                          final int level,
                          @NotNull final EntityDamageByEntityEvent event) {
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-level");
        double amountToHeal = level * multiplier;
        double newHealth = blocker.getHealth() + amountToHeal;
        if (newHealth > blocker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            newHealth = blocker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }
        blocker.setHealth(newHealth);
    }
}

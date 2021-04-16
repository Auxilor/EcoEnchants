package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Cerebral extends EcoEnchant {
    public Cerebral() {
        super(
                "cerebral", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (arrow.getLocation().getY() < victim.getLocation().getY() + victim.getEyeHeight() - 0.22) {
            return;
        }

        if (!EnchantChecks.arrow(arrow, this)) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        double damageMultiplier = (level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}

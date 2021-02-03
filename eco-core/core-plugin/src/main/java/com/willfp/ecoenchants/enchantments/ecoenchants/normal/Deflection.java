package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Deflection extends EcoEnchant {
    public Deflection() {
        super(
                "deflection", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onDeflect(@NotNull final Player blocker,
                          @NotNull final LivingEntity attacker,
                          final int level,
                          @NotNull final EntityDamageByEntityEvent event) {
        if (blocker.hasMetadata("cleaved") || blocker.hasMetadata("carved")) {
            return;
        }

        if (attacker.hasMetadata("cleaved") || attacker.hasMetadata("carved")) {
            return;
        }

        double perlevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-deflected-per-level");
        double damagePercent = (perlevel / 100) * level;
        double damage = event.getDamage() * damagePercent;

        attacker.damage(damage, attacker);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Annihilate extends EcoEnchant {
    public Annihilate() {
        super(
                "annihilate", EnchantmentType.SPECIAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        double baseMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-multiplier");
        Vector vector = attacker.getLocation().toVector().clone().subtract(victim.getLocation().toVector()).normalize().multiply(level * baseMultiplier).multiply(-1);
        if (!VectorUtils.isFinite(vector)) {
            return;
        }
        vector.setY(0.2);
        if (!VectorUtils.isFinite(vector)) {
            return;
        }
        victim.setVelocity(vector);
    }
}

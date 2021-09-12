package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.jetbrains.annotations.NotNull;

public class Rapid extends EcoEnchant {
    public Rapid() {
        super(
                "rapid", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBowShoot(@NotNull final LivingEntity shooter,
                           @NotNull final Arrow arrow,
                           final int level,
                           @NotNull final EntityShootBowEvent event) {
        if (shooter instanceof Player player) {
            AnticheatManager.exemptPlayer(player);
        }
        double multiplier = 1 - ((this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-faster-per-level") / 100) * level);

        if (event.getForce() < multiplier) {
            return;
        }

        double force = 1 / event.getForce();
        event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(force));

        if (shooter instanceof Player player) {
            AnticheatManager.unexemptPlayer(player);
        }
    }
}

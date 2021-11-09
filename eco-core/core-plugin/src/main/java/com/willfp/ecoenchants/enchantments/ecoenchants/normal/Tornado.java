package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Tornado extends EcoEnchant {
    public Tornado() {
        super(
                "tornado", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        double baseVelocity = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-per-level");
        double yVelocity = baseVelocity * level;

        Vector toAdd = new Vector(0, yVelocity, 0);

        if (victim instanceof Player pVictim) {
            AnticheatManager.exemptPlayer(pVictim);

            this.getPlugin().getScheduler().runLater(() -> {
                AnticheatManager.unexemptPlayer(pVictim);
            }, 40);
        }

        this.getPlugin().getScheduler().run(() -> {
            victim.setVelocity(victim.getVelocity().clone().add(toAdd));
        });

        this.getPlugin().getScheduler().runLater(() -> victim.setVelocity(victim.getVelocity().clone().add(toAdd)), 1);
    }
}

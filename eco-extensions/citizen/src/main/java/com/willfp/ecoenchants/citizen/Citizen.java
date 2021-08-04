package com.willfp.ecoenchants.citizen;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Citizen extends EcoEnchant {
    public Citizen() {
        super(
                "citizen", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity uncastVictim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(uncastVictim instanceof Villager)) {
            return;
        }

        event.setCancelled(true);
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(victim instanceof Villager)) {
            return;
        }

        event.setCancelled(true);
    }

    @Override
    public void onTridentDamage(@NotNull final LivingEntity attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                final int level,
                                @NotNull final EntityDamageByEntityEvent event) {
        if (!(victim instanceof Villager)) {
            return;
        }

        event.setCancelled(true);
    }
}

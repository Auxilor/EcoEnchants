package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Protector extends EcoEnchant {
    public Protector() {
        super(
                "protector", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity uncastVictim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(uncastVictim instanceof Tameable victim)) {
            return;
        }

        if (victim.getOwner() == null) {
            return;
        }
        if (!victim.getOwner().equals(attacker)) {
            return;
        }

        event.setCancelled(true);
    }
}

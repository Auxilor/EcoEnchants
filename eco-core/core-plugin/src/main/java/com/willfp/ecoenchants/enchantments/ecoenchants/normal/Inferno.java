package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jetbrains.annotations.NotNull;

public class Inferno extends EcoEnchant {
    public Inferno() {
        super(
                "inferno", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentLaunch(@NotNull LivingEntity shooter, @NotNull Trident trident, int level, @NotNull ProjectileLaunchEvent event) {
        trident.setFireTicks(Integer.MAX_VALUE);
    }

    @Override
    public void onTridentDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Trident trident, int level, @NotNull EntityDamageByEntityEvent event) {
        if(trident.getFireTicks() <= 0) return;

        victim.setFireTicks(100);
    }
}

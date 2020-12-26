package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class Atmospheric extends EcoEnchant {
    public Atmospheric() {
        super(
                "atmospheric", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @Override
    public void onTridentLaunch(@NotNull final LivingEntity shooter,
                                @NotNull final Trident trident,
                                final int level,
                                @NotNull final ProjectileLaunchEvent event) {
        if (shooter.isOnGround()) {
            return;
        }

        trident.setMetadata("shot-in-air", new FixedMetadataValue(this.getPlugin(), true));
    }

    @Override
    public void onTridentDamage(@NotNull final LivingEntity attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                final int level,
                                @NotNull final EntityDamageByEntityEvent event) {
        if (!trident.hasMetadata("shot-in-air")) {
            return;
        }

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = 1 + (multiplier * level);
        event.setDamage(damage * bonus);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class LiquidShot extends EcoEnchant {
    public LiquidShot() {
        super(
                "liquid_shot", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Arrow arrow, int level, @NotNull EntityDamageByEntityEvent event) {
        if(!(victim instanceof Blaze || victim instanceof MagmaCube || victim instanceof Enderman))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        double damageMultiplier = (level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}
package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.LightningUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Zeus extends EcoEnchant {
    public Zeus() {
        super(
                "zeus", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Arrow arrow, int level, @NotNull EntityDamageByEntityEvent event) {
        if (!EnchantmentUtils.passedChance(this, level))
            return;

        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "lightning-damage");

        LightningUtils.strike(victim, damage);
    }
}

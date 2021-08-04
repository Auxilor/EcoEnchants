package com.willfp.ecoenchants.intimidation;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Intimidation extends EcoEnchant {
    public Intimidation() {
        super(
                "intimidation", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onSwitchTarget(@NotNull final EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof Monster attacker)) {
            return;
        }

        if (event.getTarget() == null) {
            return;
        }

        if (!EnchantChecks.mainhand(event.getTarget(), this)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (this.getDisabledWorlds().contains(event.getTarget().getWorld())) {
            return;
        }

        int level = EnchantChecks.getArmorPoints(event.getTarget(), this);

        if (EnchantmentUtils.passedChance(this, level)) {
            event.setCancelled(true);
        }
    }
}

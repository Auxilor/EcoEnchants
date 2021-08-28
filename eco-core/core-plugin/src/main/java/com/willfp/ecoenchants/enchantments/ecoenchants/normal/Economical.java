package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.willfp.eco.core.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class Economical extends EcoEnchant {
    public Economical() {
        super(
                "economical", EnchantmentType.NORMAL,
                Prerequisite.HAS_PAPER
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @EventHandler
    public void onElytraBoost(@NotNull final PlayerElytraBoostEvent event) {
        if (!EnchantChecks.chestplate(event.getPlayer(), this)) {
            return;
        }
        if (!EnchantmentUtils.passedChance(this, EnchantChecks.getArmorPoints(event.getPlayer(), this))) {
            return;
        }
        if (this.getDisabledWorlds().contains(event.getPlayer().getWorld())) {
            return;
        }
        if (!this.areRequirementsMet(event.getPlayer())) {
            return;
        }
        event.setShouldConsume(false);
    }
}

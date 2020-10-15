package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.event.EventHandler;

public final class Economical extends EcoEnchant {
    public Economical() {
        super(
                new EcoEnchantBuilder("economical", EnchantmentType.NORMAL),
                new Prerequisite[]{Prerequisite.HasPaper}
        );
    }

    @EventHandler
    public void onElytraBoost(PlayerElytraBoostEvent event) {
        if(EnchantmentUtils.passedChance(this, EnchantChecks.getArmorPoints(event.getPlayer(), this)))
        event.setShouldConsume(false);
    }
}

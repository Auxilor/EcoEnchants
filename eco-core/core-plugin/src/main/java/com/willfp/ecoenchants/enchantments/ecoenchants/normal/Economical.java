package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.event.EventHandler;

public class Economical extends EcoEnchant {
    public Economical() {
        super(
                "economical", EnchantmentType.NORMAL,
                Prerequisite.HasPaper
        );
    }

    @EventHandler
    public void onElytraBoost(PlayerElytraBoostEvent event) {
        if(!EnchantChecks.chestplate(event.getPlayer(), this))
            return;
        if(!EnchantmentUtils.passedChance(this, EnchantChecks.getArmorPoints(event.getPlayer(), this)))
            return;
        if(this.getDisabledWorlds().contains(event.getPlayer().getWorld())) return;
        event.setShouldConsume(false);
    }
}

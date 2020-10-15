package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
public final class Instantaneous extends EcoEnchant {
    public Instantaneous() {
        super(
                new EcoEnchantBuilder("instantaneous", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageBlock(Player player, Block block, int level, BlockDamageEvent event) {
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        AnticheatManager.exemptPlayer(player);

        event.setInstaBreak(true);

        AnticheatManager.unexemptPlayer(player);
    }
}
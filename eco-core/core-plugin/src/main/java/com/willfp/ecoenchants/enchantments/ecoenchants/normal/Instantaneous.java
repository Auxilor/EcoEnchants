package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.eco.util.integrations.anticheat.AnticheatManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
public class Instantaneous extends EcoEnchant {
    public Instantaneous() {
        super(
                "instantaneous", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageBlock(Player player, Block block, int level, BlockDamageEvent event) {
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        if(block.getDrops(player.getInventory().getItemInMainHand()).isEmpty())
            return;

        AnticheatManager.exemptPlayer(player);

        event.setInstaBreak(true);

        AnticheatManager.unexemptPlayer(player);
    }
}
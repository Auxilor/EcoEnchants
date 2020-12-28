package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.jetbrains.annotations.NotNull;

public class Instantaneous extends EcoEnchant {
    public Instantaneous() {
        super(
                "instantaneous", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageBlock(@NotNull final Player player,
                              @NotNull final Block block,
                              final int level,
                              @NotNull final BlockDamageEvent event) {
        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (block.getDrops(player.getInventory().getItemInMainHand()).isEmpty()) {
            return;
        }

        AnticheatManager.exemptPlayer(player);

        event.setInstaBreak(true);

        AnticheatManager.unexemptPlayer(player);
    }
}

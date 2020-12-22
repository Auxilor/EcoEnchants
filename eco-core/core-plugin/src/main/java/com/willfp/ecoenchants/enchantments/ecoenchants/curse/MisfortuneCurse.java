package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class MisfortuneCurse extends EcoEnchant {
    public MisfortuneCurse() {
        super(
                "misfortune_curse", EnchantmentType.CURSE
        );
    }

    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (!EnchantmentUtils.passedChance(this, level))
            return;

        event.setDropItems(false);
    }
}
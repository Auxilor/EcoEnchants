package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
public class MisfortuneCurse extends EcoEnchant {
    public MisfortuneCurse() {
        super(
                new EcoEnchantBuilder("misfortune_curse", EnchantmentType.CURSE,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        event.setDropItems(false);
    }
}
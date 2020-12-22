package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;

public class BreaklessnessCurse extends EcoEnchant {
    public BreaklessnessCurse() {
        super(
                "breaklessness_curse", EnchantmentType.CURSE
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageBlock(Player player, Block block, int level, BlockDamageEvent event) {
        if (!EnchantmentUtils.passedChance(this, level))
            return;

        event.setCancelled(true);
    }
}
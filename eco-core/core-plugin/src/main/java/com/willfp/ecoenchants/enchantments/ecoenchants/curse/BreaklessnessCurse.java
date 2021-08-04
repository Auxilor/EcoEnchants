package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.jetbrains.annotations.NotNull;

public class BreaklessnessCurse extends EcoEnchant {
    public BreaklessnessCurse() {
        super(
                "breaklessness_curse", EnchantmentType.CURSE
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onDamageBlock(@NotNull final Player player,
                              @NotNull final Block block,
                              final int level,
                              @NotNull final BlockDamageEvent event) {
        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        event.setCancelled(true);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class Oxygenate extends EcoEnchant {
    public Oxygenate() {
        super(
                "oxygenate", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        if (player.getRemainingAir() == player.getMaximumAir()) {
            return;
        }

        int oxygenLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "oxygen-per-level");
        int oxygen = level * oxygenLevel;
        int newOxygen = player.getRemainingAir() + oxygen;
        newOxygen = NumberUtils.equalIfOver(newOxygen, player.getMaximumAir());

        player.setRemainingAir(newOxygen);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
public final class Oxygenate extends EcoEnchant {
    public Oxygenate() {
        super(
                new EcoEnchantBuilder("oxygenate", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if(player.getRemainingAir() == player.getMaximumAir()) return;

        int oxygenLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "oxygen-per-level");
        int oxygen = level * oxygenLevel;
        int newOxygen = player.getRemainingAir() + oxygen;
        newOxygen = NumberUtils.equalIfOver(newOxygen, player.getMaximumAir());

        player.setRemainingAir(newOxygen);
    }
}
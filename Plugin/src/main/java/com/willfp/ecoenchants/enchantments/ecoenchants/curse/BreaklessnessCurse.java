package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
public class BreaklessnessCurse extends EcoEnchant {
    public BreaklessnessCurse() {
        super(
                new EcoEnchantBuilder("breaklessness_curse", EnchantmentType.CURSE, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageBlock(Player player, Block block, int level, BlockDamageEvent event) {
        if (NumberUtils.randFloat(0, 1) > 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance"))
            return;

        event.setCancelled(true);
    }
}
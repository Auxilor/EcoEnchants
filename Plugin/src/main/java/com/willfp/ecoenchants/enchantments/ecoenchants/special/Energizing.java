package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Energizing extends EcoEnchant {
    public Energizing() {
        super(
                "energizing", EnchantmentType.SPECIAL
        );
    }
    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        int duration = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level") * level;
        int amplifier = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-level") + (level - 2);

        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, amplifier, true, true, true));
    }
}

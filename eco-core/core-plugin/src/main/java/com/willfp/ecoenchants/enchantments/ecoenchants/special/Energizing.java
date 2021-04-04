package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Energizing extends EcoEnchant {
    public Energizing() {
        super(
                "energizing", EnchantmentType.SPECIAL
        );
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        int duration = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level") * level;
        int amplifier = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-level") + (level - 2);

        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, amplifier, true, true, true));
    }
}

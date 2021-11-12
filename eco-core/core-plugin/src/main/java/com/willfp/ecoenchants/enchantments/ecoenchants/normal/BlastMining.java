package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BlastMining extends EcoEnchant {
    public BlastMining() {
        super(
                "blast_mining", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        if (block.hasMetadata("block-ignore")) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) {
            return;
        }

        Set<Block> toBreak = new HashSet<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "enable-sound")) {
                            block.getWorld().createExplosion(block.getLocation().clone().add(0.5, 0.5, 0.5), 0, false);
                        } else {
                            block.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, block.getLocation().clone().add(0.5, 0.5, 0.5), 1);
                        }
                        continue;
                    }
                    Block block1 = block.getWorld().getBlockAt(block.getLocation().clone().add(x, y, z));

                    if (this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blacklisted-blocks", false).contains(block1.getType().name().toLowerCase())) {
                        continue;
                    }

                    if (block1.getType().getHardness() > block.getType().getHardness() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "hardness-check")) {
                        continue;
                    }

                    if (block1.getType() == Material.AIR) {
                        continue;
                    }

                    if (!AntigriefManager.canBreakBlock(player, block1)) {
                        continue;
                    }

                    toBreak.add(block1);
                }
            }
        }

        AnticheatManager.exemptPlayer(player);
        EnchantmentUtils.rehandleBreaking(player, toBreak, this.getPlugin());
        AnticheatManager.unexemptPlayer(player);
    }
}

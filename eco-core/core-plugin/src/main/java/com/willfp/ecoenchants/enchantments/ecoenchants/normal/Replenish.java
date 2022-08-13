package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class Replenish extends EcoEnchant {
    public Replenish() {
        super(
                "replenish", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        Material type = block.getType();

        if (!(block.getBlockData() instanceof Ageable data)) {
            return;
        }

        if (block.getType() == Material.GLOW_BERRIES || block.getType() == Material.SWEET_BERRY_BUSH) {
            return;
        }

        if (block.getType() == Material.CACTUS || block.getType() == Material.BAMBOO) {
            return;
        }

        if (block.getType() == Material.CHORUS_FLOWER || block.getType() == Material.SUGAR_CANE) {
            return;
        }


        if (data.getAge() != data.getMaximumAge()) {
            event.setDropItems(false);
            event.setExpToDrop(0);

            data.setAge(0);

            this.getPlugin().getScheduler().runLater(() -> {
                block.setType(type);
                block.setBlockData(data);
            }, 1);
        }

        data.setAge(0);

        this.getPlugin().getScheduler().runLater(() -> {
            block.setType(type);
            block.setBlockData(data);
        }, 1);
    }
}

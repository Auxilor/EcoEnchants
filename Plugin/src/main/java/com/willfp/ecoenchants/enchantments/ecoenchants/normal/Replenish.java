package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
public final class Replenish extends EcoEnchant {
    public Replenish() {
        super(
                new EcoEnchantBuilder("replenish", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        Material type = block.getType();

        if(!(block.getBlockData() instanceof Ageable)) return;

        Ageable data = (Ageable) block.getBlockData();
        if(data.getAge() != data.getMaximumAge()) {
            event.setDropItems(false);
            event.setExpToDrop(0);

            data.setAge(0);

            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(type);
                    block.setBlockData(data);
                }
            }.runTaskLater(EcoEnchantsPlugin.getInstance(), 1);
        }

        data.setAge(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(type);
                block.setBlockData(data);
            }
        }.runTaskLater(EcoEnchantsPlugin.getInstance(), 1);
    }
}

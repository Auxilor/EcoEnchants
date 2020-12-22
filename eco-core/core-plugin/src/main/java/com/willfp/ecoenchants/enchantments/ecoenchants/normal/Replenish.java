package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
public class Replenish extends EcoEnchant {
    public Replenish() {
        super(
                "replenish", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        Material type = block.getType();

        if(!(block.getBlockData() instanceof Ageable)) return;

        if(block.getType().equals(Material.SUGAR_CANE) || block.getType().equals(Material.SWEET_BERRY_BUSH))
            return;

        Ageable data = (Ageable) block.getBlockData();
        if(data.getAge() != data.getMaximumAge()) {
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

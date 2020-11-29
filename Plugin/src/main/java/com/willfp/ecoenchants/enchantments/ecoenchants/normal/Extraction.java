package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.util.internal.DropQueue;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Extraction extends EcoEnchant {
    public Extraction() {
        super(
                "extraction", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        Material material = null;

        if(block.getType().equals(Material.GOLD_ORE)) material = Material.GOLD_NUGGET;
        if(block.getType().equals(Material.IRON_ORE)) material = Material.IRON_NUGGET;

        if(material == null) return;

        ItemStack item = new ItemStack(material, 1);

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItem(item)
                .push();
    }
}

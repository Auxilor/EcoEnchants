package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.util.NumberUtils;
import com.willfp.ecoenchants.util.internal.DropQueue;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class Transfuse extends EcoEnchant {
    public Transfuse() {
        super(
                "transfuse", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        event.setDropItems(false);

        if(!this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "works-on").stream().map(string -> Material.getMaterial(string.toUpperCase())).collect(Collectors.toList()).contains(block.getType()))
            return;

        Material material;
        double random = NumberUtils.randFloat(0, 1);
        double band = 1/(double) this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").size();
        int selectedIndex = (int) Math.floor(random/band);
        selectedIndex = NumberUtils.equalIfOver(selectedIndex, this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").size() - 1);
        String materialName = this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").get(selectedIndex);
        material = Material.getMaterial(materialName.toUpperCase());
        if(material == null) material = Material.COBBLESTONE;

        ItemStack item = new ItemStack(material, 1);

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItem(item)
                .push();
    }
}

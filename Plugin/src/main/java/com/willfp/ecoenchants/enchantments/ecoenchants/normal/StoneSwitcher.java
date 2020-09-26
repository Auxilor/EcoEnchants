package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.queue.DropQueue;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
public class StoneSwitcher extends EcoEnchant {
    public StoneSwitcher() {
        super(
                new EcoEnchantBuilder("stone_switcher", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if(!block.getType().equals(Material.STONE)) return;

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");

        if(NumberUtils.randFloat(0, 1) > level * chance * 0.01)
            return;

        event.setDropItems(false);

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

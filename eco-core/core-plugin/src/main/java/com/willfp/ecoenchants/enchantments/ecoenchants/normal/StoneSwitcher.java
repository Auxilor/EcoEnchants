package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StoneSwitcher extends EcoEnchant {
    public StoneSwitcher() {
        super(
                "stone_switcher", EnchantmentType.NORMAL
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
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (!block.getType().equals(Material.STONE)) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        event.setDropItems(false);

        Material material;
        double random = NumberUtils.randFloat(0, 1);
        double band = 1 / (double) this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").size();
        int selectedIndex = (int) Math.floor(random / band);
        selectedIndex = NumberUtils.equalIfOver(selectedIndex, this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").size() - 1);
        String materialName = this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").get(selectedIndex);
        material = Material.getMaterial(materialName.toUpperCase());

        if (material == null) {
            material = Material.COBBLESTONE;
        }

        ItemStack item = new ItemStack(material, 1);

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItem(item)
                .push();
    }
}

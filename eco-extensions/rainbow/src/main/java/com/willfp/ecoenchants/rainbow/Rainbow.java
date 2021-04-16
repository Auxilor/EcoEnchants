package com.willfp.ecoenchants.rainbow;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rainbow extends EcoEnchant {
    public Rainbow() {
        super(
                "rainbow", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (!Tag.LEAVES.isTagged(block.getType())) {
            return;
        }

        event.setDropItems(false);

        Material toDrop;

        List<Material> materials = new ArrayList<>();

        for (String materialName : this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "items")) {
            Material material = Material.getMaterial(materialName.toUpperCase());
            if (material != null) {
                materials.add(material);
            }
        }

        toDrop = materials.get(new Random().nextInt(materials.size()));

        if (toDrop == null) {
            toDrop = block.getType();
        }

        ItemStack item = new ItemStack(toDrop, 1);

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItem(item)
                .push();
    }
}

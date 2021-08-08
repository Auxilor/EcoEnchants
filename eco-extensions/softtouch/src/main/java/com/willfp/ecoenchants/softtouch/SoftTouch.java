package com.willfp.ecoenchants.softtouch;


import com.willfp.eco.core.drops.DropQueue;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

public class SoftTouch extends EcoEnchant {
    public SoftTouch() {
        super("soft_touch", EnchantmentType.NORMAL);
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (!(block.getState() instanceof CreatureSpawner spawner)) {
            return;
        }

        EntityType type = spawner.getSpawnedType();

        event.setDropItems(false);
        event.setExpToDrop(0);

        ItemStack itemStack = new ItemStack(Material.SPAWNER);
        BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setBlockState(spawner);
        itemStack.setItemMeta(meta);

        new DropQueue(player)
                .addItem(itemStack)
                .push();
    }
}

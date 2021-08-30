package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.BlockUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lumberjack extends EcoEnchant {
    public Lumberjack() {
        super(
                "lumberjack", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        if (block.hasMetadata("block-ignore")) {
            return;
        }

        if (player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) {
            return;
        }

        List<Material> materials = new ArrayList<>();
        this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "whitelisted-blocks").forEach(name -> materials.add(Material.getMaterial(name.toUpperCase())));

        if (!materials.contains(block.getType())) {
            return;
        }

        int blocksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "blocks-per-level");
        int limit = level * blocksPerLevel;

        Set<Block> treeBlocks = BlockUtils.getVein(block, materials, limit);
        treeBlocks.removeIf(block1 -> !AntigriefManager.canBreakBlock(player, block1));

        AnticheatManager.exemptPlayer(player);
        EnchantmentUtils.rehandleBreaking(player, treeBlocks, this.getPlugin());
        AnticheatManager.unexemptPlayer(player);
    }
}

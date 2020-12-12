package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.BlockBreak;
import com.willfp.ecoenchants.util.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Vein extends EcoEnchant {
    public Vein() {
        super(
                "vein", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (block.hasMetadata("block-ignore"))
            return;

        if (player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) return;

        List<Material> materials = new ArrayList<>();
        this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "whitelisted-blocks").forEach(name -> materials.add(Material.getMaterial(name.toUpperCase())));

        if (!materials.contains(block.getType()))
            return;

        int blocksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "blocks-per-level");
        int limit = level * blocksPerLevel;

        Set<Block> blockSet = BlockUtils.getVein(block, materials, limit);

        AnticheatManager.exemptPlayer(player);

        for (Block veinBlock : blockSet) {
            veinBlock.setMetadata("block-ignore", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));
            if (!AntigriefManager.canBreakBlock(player, veinBlock)) continue;

            BlockBreak.breakBlock(player, veinBlock);

            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> veinBlock.removeMetadata("block-ignore", EcoEnchantsPlugin.getInstance()), 1);
        }

        AnticheatManager.unexemptPlayer(player);
    }
}
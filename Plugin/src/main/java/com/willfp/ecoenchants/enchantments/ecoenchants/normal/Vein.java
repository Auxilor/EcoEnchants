package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.BlockBreak;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.RecursiveBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Vein extends EcoEnchant {
    public Vein() {
        super(
                new EcoEnchantBuilder("vein", EnchantmentType.NORMAL, Target.Applicable.PICKAXE, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.hasMetadata("from-drill") || block.hasMetadata("from-blastmining") || block.hasMetadata("from-lumberjack") || block.hasMetadata("from-vein")) {
            return;
        }

        if (!HasEnchant.playerHeld(player, this)) return;

        if (event.isCancelled())
            return;

        if(!AntigriefManager.canBreakBlock(player, block)) return;

        if(player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) return;

        List<Material> materials = new ArrayList<>();
        this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "whitelisted-blocks").forEach(name -> materials.add(Material.getMaterial(name.toUpperCase())));

        if(!materials.contains(block.getType()))
            return;

        int blocksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "blocks-per-level");
        int level = HasEnchant.getPlayerLevel(player, this);
        int limit = level * blocksPerLevel;

        Set<Block> blockSet = RecursiveBlock.getVein(block, materials, limit);

        AnticheatManager.exemptPlayer(player);

        for(Block veinBlock : blockSet) {
            veinBlock.setMetadata("from-vein", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));
            if(!AntigriefManager.canBreakBlock(player, veinBlock)) continue;

            BlockBreak.breakBlock(player, veinBlock);

            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> veinBlock.removeMetadata("from-vein", EcoEnchantsPlugin.getInstance()),1);
        }

        AnticheatManager.unexemptPlayer(player);
    }
}
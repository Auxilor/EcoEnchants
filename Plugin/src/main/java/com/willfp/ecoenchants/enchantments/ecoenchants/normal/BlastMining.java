package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.BlockBreak;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
public class BlastMining extends EcoEnchant {
    public BlastMining() {
        super(
                new EcoEnchantBuilder("blast_mining", EnchantmentType.NORMAL, Target.Applicable.PICKAXE, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.hasMetadata("from-drill") || block.hasMetadata("from-lumberjack") || block.hasMetadata("from-blastmining") || block.hasMetadata("from-vein")) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) return;

        if (event.isCancelled())
            return;

        if(!AntigriefManager.canBreakBlock(player, block)) return;

        if(player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) return;

        boolean hasExploded = false;

        AnticheatManager.exemptPlayer(player);

        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if(x == 0 && y == 0 && z == 0) continue;
                    Block block1 = block.getWorld().getBlockAt(block.getLocation().clone().add(x, y, z));
                    block1.setMetadata("from-blastmining", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));

                    if(this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blacklisted-blocks").contains(block1.getType().name().toLowerCase())) {
                        continue;
                    }

                    if(block1.getType().getHardness() > block.getType().getHardness() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "hardness-check")) continue;

                    if(!AntigriefManager.canBreakBlock(player, block1)) continue;

                    BlockBreak.breakBlock(player, block1);
                    if(!hasExploded) {
                        block.getWorld().createExplosion(block.getLocation().clone().add(0.5, 0.5, 0.5), 0, false);
                        hasExploded = true;
                    }
                    block1.removeMetadata("from-blastmining", EcoEnchantsPlugin.getInstance());
                }
            }
        }

        AnticheatManager.unexemptPlayer(player);
    }
}
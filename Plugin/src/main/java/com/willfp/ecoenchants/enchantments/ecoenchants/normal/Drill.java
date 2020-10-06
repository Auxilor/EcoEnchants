package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.BlockBreak;
import com.willfp.ecoenchants.util.VectorUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
public final class Drill extends EcoEnchant {
    public Drill() {
        super(
                new EcoEnchantBuilder("drill", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (block.hasMetadata("from-drill") || block.hasMetadata("from-lumberjack") || block.hasMetadata("from-blastmining") || block.hasMetadata("from-vein")) {
            return;
        }

        if(player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) return;

        int blocks = level * this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "blocks-per-level");

        AnticheatManager.exemptPlayer(player);

        for(int i = 1; i <= blocks; i++) {
            Vector simplified = VectorUtils.simplifyVector(player.getLocation().getDirection().normalize()).multiply(i);
            Block block1 = block.getWorld().getBlockAt(block.getLocation().clone().add(simplified));
            block1.setMetadata("from-drill", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));

            if(this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blacklisted-blocks").contains(block1.getType().name().toLowerCase())) {
                continue;
            }

            if(block1.getType().getHardness() > block.getType().getHardness() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "hardness-check")) continue;

            if(!AntigriefManager.canBreakBlock(player, block1)) continue;

            BlockBreak.breakBlock(player, block1);
            block1.removeMetadata("from-drill", EcoEnchantsPlugin.getInstance());
        }

        AnticheatManager.unexemptPlayer(player);
    }
}
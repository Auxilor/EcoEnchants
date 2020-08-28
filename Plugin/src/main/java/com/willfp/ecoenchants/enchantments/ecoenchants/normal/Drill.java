package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.anticheat.AnticheatManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.BlockBreak;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.AntiGrief;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.SimplifyVector;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class Drill extends EcoEnchant {
    public Drill() {
        super(
                new EcoEnchantBuilder("drill", EnchantmentType.NORMAL, Target.Applicable.TOOL, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler(priority = EventPriority.LOW)
    public void drillBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.hasMetadata("from-drill") || block.hasMetadata("from-lumberjack") || block.hasMetadata("from-blastmining") || block.hasMetadata("from-vein")) {
            return;
        }

        if (!HasEnchant.playerHeld(player, this)) return;

        if (event.isCancelled())
            return;

        if (!AntiGrief.canBreakBlock(player, block)) return;

        if(player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) return;

        int level = HasEnchant.getPlayerLevel(player, this) * this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "blocks-per-level");

        AnticheatManager.exemptPlayer(player);

        for(int i = 1; i <= level; i++) {
            Vector simplified = SimplifyVector.simplifyVector(player.getLocation().getDirection().normalize()).multiply(i);
            Block block1 = block.getWorld().getBlockAt(block.getLocation().clone().add(simplified));
            block1.setMetadata("from-drill", new FixedMetadataValue(Main.getInstance(), true));

            if(this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blacklisted-blocks").contains(block1.getType().name().toLowerCase())) {
                continue;
            }

            if(block1.getType().getHardness() > block.getType().getHardness() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "hardness-check")) continue;

            if (!AntiGrief.canBreakBlock(player, block1)) continue;

            BlockBreak.breakBlock(player, block1);
            block1.removeMetadata("from-drill", Main.getInstance());
        }

        AnticheatManager.unexemptPlayer(player);
    }
}
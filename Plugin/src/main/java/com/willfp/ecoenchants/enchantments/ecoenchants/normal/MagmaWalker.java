package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.Circle;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MagmaWalker extends EcoEnchant {
    public MagmaWalker() {
        super(
                new EcoEnchantBuilder("magma_walker", EnchantmentType.NORMAL, Target.Applicable.BOOTS, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onLavaWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(event.getTo() == null) return;
        if(event.getFrom().getBlock().equals(event.getTo().getBlock())) return;

        if(!HasEnchant.playerBoots(player, this)) return;

        Vector[] circle = Circle.getCircle(this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-radius")
                + (this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-radius") * HasEnchant.getPlayerBootsLevel(player, this) - 1));

        AnticheatManager.exemptPlayer(player);

        for(Vector vector : circle) {
            Location loc = player.getLocation().add(vector).add(0, -1, 0);

            Block block = player.getWorld().getBlockAt(loc);

            if(!AntigriefManager.canPlaceBlock(player, player.getWorld().getBlockAt(loc))) continue;

            if(!block.getType().equals(Material.LAVA)) continue;

            Levelled data = (Levelled) block.getBlockData();

            if(data.getLevel() != 0) continue;

            block.setType(Material.OBSIDIAN);

            block.setMetadata("byMagmaWalker", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));

            long afterTicks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "remove-after-ticks");

            BukkitRunnable replace = new BukkitRunnable() {
                @Override
                public void run() {
                    if(block.getType().equals(Material.OBSIDIAN)) {
                        if(!player.getWorld().getBlockAt(player.getLocation().add(0, -1, 0)).equals(block)) {
                            block.setType(Material.LAVA);
                            block.removeMetadata("byMagmaWalker", EcoEnchantsPlugin.getInstance());
                            this.cancel();
                        }
                    }
                }
            };

            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
                if(block.getType().equals(Material.OBSIDIAN)) {
                    if(!player.getWorld().getBlockAt(player.getLocation().add(0, -1, 0)).equals(block)) {
                        block.setType(Material.LAVA);
                        block.removeMetadata("byMagmaWalker", EcoEnchantsPlugin.getInstance());
                    } else {
                        replace.runTaskTimer(EcoEnchantsPlugin.getInstance(), afterTicks, afterTicks);
                    }
                }
            }, afterTicks);
        }

        AnticheatManager.unexemptPlayer(player);
    }

}

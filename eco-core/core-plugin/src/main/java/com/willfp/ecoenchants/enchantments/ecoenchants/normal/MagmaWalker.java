package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.core.scheduling.RunnableTask;
import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class MagmaWalker extends EcoEnchant {
    public MagmaWalker() {
        super(
                "magma_walker", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onLavaWalk(@NotNull final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo() == null) {
            return;
        }

        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        if (!EnchantChecks.boots(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        Vector[] circle = VectorUtils.getCircle(this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-radius")
                + (this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-radius") * EnchantChecks.getBootsLevel(player, this) - 1));

        AnticheatManager.exemptPlayer(player);

        for (Vector vector : circle) {
            Location loc = player.getLocation().add(vector).add(0, -1, 0);

            Block block = player.getWorld().getBlockAt(loc);

            if (!AntigriefManager.canPlaceBlock(player, player.getWorld().getBlockAt(loc))) {
                continue;
            }

            if (!block.getType().equals(Material.LAVA)) {
                continue;
            }

            Levelled data = (Levelled) block.getBlockData();

            if (data.getLevel() != 0) {
                continue;
            }

            block.setType(Material.OBSIDIAN);

            block.setMetadata("byMagmaWalker", this.getPlugin().getMetadataValueFactory().create(true));

            long afterTicks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "remove-after-ticks");

            RunnableTask replace = this.getPlugin().getRunnableFactory().create(bukkitRunnable -> {
                if (block.getType().equals(Material.OBSIDIAN) && !player.getWorld().getBlockAt(player.getLocation().add(0, -1, 0)).equals(block)) {
                    block.setType(Material.LAVA);
                    block.removeMetadata("byMagmaWalker", this.getPlugin());
                    bukkitRunnable.cancel();
                }
            });

            this.getPlugin().getScheduler().runLater(() -> {
                if (block.getType().equals(Material.OBSIDIAN)) {
                    if (!player.getWorld().getBlockAt(player.getLocation().add(0, -1, 0)).equals(block)) {
                        block.setType(Material.LAVA);
                        block.removeMetadata("byMagmaWalker", this.getPlugin());
                    } else {
                        replace.runTaskTimer(afterTicks, afterTicks);
                    }
                }
            }, afterTicks);
        }

        AnticheatManager.unexemptPlayer(player);
    }

}

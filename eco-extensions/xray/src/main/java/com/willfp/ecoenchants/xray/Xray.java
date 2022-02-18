package com.willfp.ecoenchants.xray;

import com.willfp.eco.util.TeamUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Xray extends Spell {
    public Xray() {
        super("xray");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                      final int level,
                      @NotNull final PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return false;
        }

        Location location;

        if (block == null) {
            location = player.getLocation();
        } else {
            location = block.getLocation();
        }

        Set<Block> toReveal = new HashSet<>();

        int size = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "blocks-per-level") * level;

        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks");

        List<Material> materials = new ArrayList<>();

        for (String materialName : this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks")) {
            Material material = Material.getMaterial(materialName.toUpperCase());
            if (material != null) {
                materials.add(material);
            }
        }

        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                for (int z = -size; z <= size; z++) {
                    Block block1 = location.getWorld().getBlockAt(location.clone().add(x, y, z));

                    if (!materials.contains(block1.getType())) {
                        continue;
                    }

                    toReveal.add(block1);
                }
            }
        }

        toReveal.forEach(block1 -> {
            Shulker shulker = (Shulker) block1.getWorld().spawnEntity(block1.getLocation(), EntityType.SHULKER);
            shulker.setInvulnerable(true);
            shulker.setSilent(true);
            shulker.setAI(false);
            shulker.setGravity(false);
            shulker.setGlowing(true);
            shulker.setInvisible(true);
            shulker.setMetadata("xray-shulker", this.getPlugin().getMetadataValueFactory().create(true));

            block1.setMetadata("xray-uuid", this.getPlugin().getMetadataValueFactory().create(shulker.getUniqueId()));

            if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "color-glow")) {
                @SuppressWarnings("deprecation")
                Team team = TeamUtils.getMaterialColorTeam(block1.getType());
                team.addEntry(shulker.getUniqueId().toString());
            }

            this.getPlugin().getScheduler().runLater(() -> {
                shulker.remove();
                block1.removeMetadata("xray-uuid", this.getPlugin());
            }, ticks);
        });

        return true;
    }

    @EventHandler
    public void onBlockBreak(@NotNull final BlockBreakEvent event) {
        Block block = event.getBlock();

        if (!block.hasMetadata("xray-uuid")) {
            return;
        }

        for (MetadataValue meta : block.getMetadata("xray-uuid")) {
            if (!(meta.value() instanceof UUID uuid)) {
                continue;
            }

            assert uuid != null;

            Entity entity = Bukkit.getServer().getEntity(uuid);

            if (entity != null) {
                entity.remove();
            }
        }

        for (Entity shulkerEntity : block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2, entity -> entity.hasMetadata("xray-shulker"))) {
            shulkerEntity.remove();
        }
    }
}

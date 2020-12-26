package com.willfp.eco.util.integrations.antigrief.plugins;

import com.willfp.eco.util.integrations.antigrief.AntigriefWrapper;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.kingdoms.constants.kingdom.Kingdom;
import org.kingdoms.constants.land.Land;
import org.kingdoms.managers.PvPManager;
import org.kingdoms.managers.land.LandManager;

public class AntigriefKingdoms implements AntigriefWrapper {
    @Override
    public boolean canBreakBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        LandManager.onBreak(event);
        return !event.isCancelled();
    }

    @Override
    public boolean canCreateExplosion(@NotNull final Player player,
                                      @NotNull final Location location) {
        Land land = Land.getLand(location);
        if (land == null) {
            return true;
        }
        if (!land.isClaimed()) {
            return true;
        }

        Kingdom kingdom = land.getKingdom();
        return kingdom.isMember(player);
    }

    @Override
    public boolean canPlaceBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        Block placedOn = block.getRelative(0, -1, 0);
        BlockPlaceEvent event = new BlockPlaceEvent(block, block.getState(), placedOn, player.getInventory().getItemInMainHand(), player, true, EquipmentSlot.HAND);
        LandManager.onPlace(event);
        return !event.isCancelled();
    }

    @Override
    public boolean canInjure(@NotNull final Player player,
                             @NotNull final LivingEntity victim) {
        if (victim instanceof Player) {
            return PvPManager.canFight(player, (Player) victim);
        } else {
            Land land = Land.getLand(victim.getLocation());
            if (land == null) {
                return true;
            }
            if (!land.isClaimed()) {
                return true;
            }

            Kingdom kingdom = land.getKingdom();
            return kingdom.isMember(player);
        }
    }

    @Override
    public String getPluginName() {
        return "Kingdoms";
    }
}

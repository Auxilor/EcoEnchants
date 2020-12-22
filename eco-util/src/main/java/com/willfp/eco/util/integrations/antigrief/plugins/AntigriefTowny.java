package com.willfp.eco.util.integrations.antigrief.plugins;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.object.WorldCoord;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.willfp.eco.util.integrations.antigrief.AntigriefWrapper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AntigriefTowny implements AntigriefWrapper {
    @Override
    public boolean canBreakBlock(Player player, Block block) {
        return PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.DESTROY);
    }

    @Override
    public boolean canCreateExplosion(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission(player, location, Material.TNT, TownyPermission.ActionType.ITEM_USE);
    }

    @Override
    public boolean canPlaceBlock(Player player, Block block) {
        return PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.BUILD);
    }

    @Override
    public boolean canInjure(Player player, LivingEntity victim) {
        if(victim instanceof Player) {
            try {
                Town town = WorldCoord.parseWorldCoord(victim.getLocation()).getTownBlock().getTown();
                return town.isPVP();
            } catch (Exception ignored) {}
        } else {
            try {
                Town town = WorldCoord.parseWorldCoord(victim.getLocation()).getTownBlock().getTown();
                return town.hasMobs();
            } catch (Exception ignored) {}
        }
        return true;
    }

    @Override
    public String getPluginName() {
        return "Towny";
    }
}

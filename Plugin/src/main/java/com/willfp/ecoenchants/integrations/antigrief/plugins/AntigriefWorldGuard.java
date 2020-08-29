package com.willfp.ecoenchants.integrations.antigrief.plugins;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefWrapper;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AntigriefWorldGuard implements AntigriefWrapper {
    @Override
    public boolean canBreakBlock(Player player, Block block) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if(!query.testState(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BUILD)) {
            return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(block.getWorld()));
        }
        return true;
    }

    @Override
    public boolean canCreateExplosion(Player player, Location location) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if(!query.testState(BukkitAdapter.adapt(location), localPlayer, Flags.OTHER_EXPLOSION)) {
            return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(location.getWorld()));
        }
        return true;
    }

    @Override
    public boolean canPlaceBlock(Player player, Block block) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if(!query.testState(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BLOCK_PLACE)) {
            return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(block.getWorld()));
        }
        return true;
    }

    @Override
    public boolean canInjure(Player player, LivingEntity victim) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if(victim instanceof Player) {
            if(!query.testState(BukkitAdapter.adapt(victim.getLocation()), localPlayer, Flags.PVP)) {
                return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(player.getWorld()));
            }
        } else {
            if(!query.testState(BukkitAdapter.adapt(victim.getLocation()), localPlayer, Flags.DAMAGE_ANIMALS)) {
                return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(player.getWorld()));
            }
        }
        return true;
    }

    @Override
    public String getPluginName() {
        return "WorldGuard";
    }
}

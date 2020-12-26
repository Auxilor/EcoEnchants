package com.willfp.eco.util.integrations.antigrief.plugins;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.willfp.eco.util.integrations.antigrief.AntigriefWrapper;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AntigriefWorldGuard implements AntigriefWrapper {
    @Override
    public boolean canBreakBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if (query.queryState(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BUILD) == StateFlag.State.DENY) {
            return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(block.getWorld()));
        }
        return true;
    }

    @Override
    public boolean canCreateExplosion(@NotNull final Player player,
                                      @NotNull final Location location) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        World world = location.getWorld();
        Validate.notNull(world, "World cannot be null!");

        if (query.queryState(BukkitAdapter.adapt(location), localPlayer, Flags.OTHER_EXPLOSION) == StateFlag.State.DENY) {
            return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(world));
        }
        return true;
    }

    @Override
    public boolean canPlaceBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if (query.queryState(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BLOCK_PLACE) == StateFlag.State.DENY) {
            return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(block.getWorld()));
        }
        return true;
    }

    @Override
    public boolean canInjure(@NotNull final Player player,
                             @NotNull final LivingEntity victim) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if (victim instanceof Player) {
            if (query.queryState(BukkitAdapter.adapt(victim.getLocation()), localPlayer, Flags.PVP) == StateFlag.State.DENY) {
                return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(player.getWorld()));
            }
        } else {
            if (query.queryState(BukkitAdapter.adapt(victim.getLocation()), localPlayer, Flags.DAMAGE_ANIMALS) == StateFlag.State.DENY) {
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

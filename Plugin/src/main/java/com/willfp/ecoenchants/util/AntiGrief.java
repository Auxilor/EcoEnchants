package com.willfp.ecoenchants.util;

import com.massivecraft.factions.*;
import com.massivecraft.factions.perms.PermissibleAction;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.object.WorldCoord;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.willfp.ecoenchants.Main;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Class containing methods for compatibility with land management plugins
 */
public class AntiGrief {

    /**
     * Can player break block
     * @param player The player
     * @param block The block
     * @return If player can break block
     */
    public static boolean canBreakBlock(Player player, Block block) {
        if (Main.hasWG) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            if (!query.testState(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BUILD)) {
                if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(block.getWorld()))) {
                    return false;
                }
            }
        }

        if (Main.hasGP) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);
            if (claim != null) {
                if (claim.allowBreak(player, block.getType()) != null) {
                    return false;
                }
            }
        }

        if (Main.hasFactionsUUID) {
            FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
            FLocation flocation = new FLocation(block.getLocation());
            Faction faction = Board.getInstance().getFactionAt(flocation);

            if (!faction.hasAccess(fplayer, PermissibleAction.DESTROY)) {
                if (!fplayer.isAdminBypassing()) {
                    return false;
                }
            }
        }

        if (Main.hasTowny) {
            if (!PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.DESTROY)) {
                return false;
            }
        }

        if (Main.hasLands) {
            LandsIntegration landsIntegration = new LandsIntegration(Main.getInstance());
            Area area = landsIntegration.getAreaByLoc(block.getLocation());
            if (area != null) {
                return area.canSetting(player, RoleSetting.BLOCK_BREAK, false);
            }
        }


        return true;

    }

    /**
     * Can player create explosion at location
     * @param player The player
     * @param location The location
     * @return If player can create explosion
     */
    public static boolean canCreateExplosion(Player player, Location location) {
        if (Main.hasWG) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            if (!query.testState(BukkitAdapter.adapt(location), localPlayer, Flags.OTHER_EXPLOSION)) {
                if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(location.getWorld()))) {
                    return false;
                }
            }
        }

        if (Main.hasGP) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
            if (claim != null) {
                if (!claim.areExplosivesAllowed) {
                    return false;
                }
            }
        }

        if (Main.hasFactionsUUID) {
            FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
            FLocation flocation = new FLocation(location);
            Faction faction = Board.getInstance().getFactionAt(flocation);

            if (faction.noExplosionsInTerritory()) {
                return false;
            }
        }

        if (Main.hasTowny) {
            if (!PlayerCacheUtil.getCachePermission(player, location, Material.TNT, TownyPermission.ActionType.ITEM_USE)) {
                return false;
            }
        }

        if (Main.hasLands) {
            LandsIntegration landsIntegration = new LandsIntegration(Main.getInstance());
            Area area = landsIntegration.getAreaByLoc(location);
            if (area != null) {
                return area.canSetting(player, RoleSetting.BLOCK_IGNITE, false);
            }
        }


        return true;
    }

    /**
     * Can player place block
     * @param player The player
     * @param block The block
     * @return If player can place block
     */
    public static boolean canPlaceBlock(Player player, Block block) {
        if (Main.hasWG) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            if (!query.testState(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BLOCK_PLACE)) {
                if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(block.getWorld()))) {
                    return false;
                }
            }
        }

        if (Main.hasGP) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);
            if (claim != null) {
                if (claim.allowBuild(player, block.getType()) != null) {
                    return false;
                }
            }
        }

        if (Main.hasFactionsUUID) {
            FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
            FLocation flocation = new FLocation(block.getLocation());
            Faction faction = Board.getInstance().getFactionAt(flocation);

            if (!faction.hasAccess(fplayer, PermissibleAction.BUILD)) {
                if (!fplayer.isAdminBypassing()) {
                    return false;
                }
            }
        }

        if (Main.hasTowny) {
            if (!PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.BUILD)) {
                return false;
            }
        }

        if (Main.hasLands) {
            LandsIntegration landsIntegration = new LandsIntegration(Main.getInstance());
            Area area = landsIntegration.getAreaByLoc(block.getLocation());
            if (area != null) {
                return area.canSetting(player, RoleSetting.BLOCK_PLACE, false);
            }
        }

        return true;

    }

    /**
     * Can player injure other player
     * @param player The player
     * @param victim The victim
     * @return If player can injure player
     */
    public static boolean canInjurePlayer(Player player, Player victim) {
        if (Main.hasWG) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            if (!query.testState(BukkitAdapter.adapt(victim.getLocation()), localPlayer, Flags.PVP)) {
                if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(player.getWorld()))) {
                    return false;
                }
            }
        }

        if (Main.hasGP) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(victim.getLocation(), false, null);
            if (claim != null) {
                return false;
            }
        }

        if (Main.hasFactionsUUID) {
            FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
            FLocation flocation = new FLocation(victim.getLocation());
            Faction faction = Board.getInstance().getFactionAt(flocation);

            if (faction.isPeaceful()) {
                if (!fplayer.isAdminBypassing()) {
                    return false;
                }
            }
        }

        if (Main.hasTowny) {
            try {
                Town town = WorldCoord.parseWorldCoord(victim.getLocation()).getTownBlock().getTown();
                if (!town.isPVP()) {
                    return false;
                }
            } catch (Exception ignored) {}
        }

        if (Main.hasLands) {
            LandsIntegration landsIntegration = new LandsIntegration(Main.getInstance());
            Area area = landsIntegration.getAreaByLoc(victim.getLocation());
            if (area != null) {
                return area.canSetting(player, RoleSetting.ATTACK_PLAYER, false);
            }
        }

        return true;
    }

    /**
     * Can player injure mob
     * @param player The player
     * @param victim The victim
     * @return If player can injure player
     */
    public static boolean canInjureMob(Player player, LivingEntity victim) {
        if (Main.hasWG) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            if (!query.testState(BukkitAdapter.adapt(victim.getLocation()), localPlayer, Flags.DAMAGE_ANIMALS)) {
                if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(player.getWorld()))) {
                    return false;
                }
            }
        }

        if (Main.hasGP) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(victim.getLocation(), false, null);
            if (claim != null) {
                if(!claim.ownerID.equals(player.getUniqueId())) {
                    return false;
                }
            }
        }

        if (Main.hasFactionsUUID) {
            FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
            FLocation flocation = new FLocation(victim.getLocation());
            Faction faction = Board.getInstance().getFactionAt(flocation);

            if (faction.hasAccess(fplayer, PermissibleAction.DESTROY)) {
                if (!fplayer.isAdminBypassing()) {
                    return false;
                }
            }
        }

        if (Main.hasTowny) {
            try {
                Town town = WorldCoord.parseWorldCoord(victim.getLocation()).getTownBlock().getTown();
                if (!town.hasMobs()) {
                    return false;
                }
            } catch (Exception ignored) {}
        }

        if (Main.hasLands) {
            LandsIntegration landsIntegration = new LandsIntegration(Main.getInstance());
            Area area = landsIntegration.getAreaByLoc(victim.getLocation());
            if (area != null) {
                return area.canSetting(player, RoleSetting.ATTACK_ANIMAL, false);
            }
        }

        return true;
    }

    /**
     * Does player have keep inventory
     * @param player The player
     * @return If player has keep inventory
     */
    public static boolean hasKeepInv(Player player) {
        return false;
    }
}

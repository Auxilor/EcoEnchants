package com.willfp.eco.util.integrations.antigrief.plugins;

import com.willfp.eco.util.integrations.antigrief.AntigriefWrapper;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AntigriefGriefPrevention implements AntigriefWrapper {
    @Override
    public boolean canBreakBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);
        if (claim != null) {
            return claim.allowBreak(player, block.getType()) == null;
        }
        return true;
    }

    @Override
    public boolean canCreateExplosion(@NotNull final Player player,
                                      @NotNull final Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
        if (claim != null) {
            return claim.areExplosivesAllowed;
        }
        return true;
    }

    @Override
    public boolean canPlaceBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);
        if (claim != null) {
            return claim.allowBuild(player, block.getType()) == null;
        }
        return true;
    }

    @Override
    public boolean canInjure(@NotNull final Player player,
                             @NotNull final LivingEntity victim) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(victim.getLocation(), false, null);
        if (victim instanceof Player) {
            return claim == null;
        } else {
            if (claim != null && claim.ownerID != null) {
                return claim.ownerID.equals(player.getUniqueId());
            }
            return true;
        }
    }

    @Override
    public String getPluginName() {
        return "GriefPrevention";
    }
}

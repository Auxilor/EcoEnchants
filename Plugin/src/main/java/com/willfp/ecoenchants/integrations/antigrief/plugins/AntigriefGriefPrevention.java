package com.willfp.ecoenchants.integrations.antigrief.plugins;

import com.willfp.ecoenchants.integrations.antigrief.AntigriefWrapper;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AntigriefGriefPrevention implements AntigriefWrapper {
    @Override
    public boolean canBreakBlock(Player player, Block block) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);
        if (claim != null) {
            return claim.allowBreak(player, block.getType()) == null;
        }
        return true;
    }

    @Override
    public boolean canCreateExplosion(Player player, Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
        if (claim != null) {
            return claim.areExplosivesAllowed;
        }
        return true;
    }

    @Override
    public boolean canPlaceBlock(Player player, Block block) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);
        if (claim != null) {
            return claim.allowBuild(player, block.getType()) == null;
        }
        return true;
    }

    @Override
    public boolean canInjure(Player player, LivingEntity victim) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(victim.getLocation(), false, null);
        if(victim instanceof Player) {
            return claim == null;
        } else {
            if (claim != null) {
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

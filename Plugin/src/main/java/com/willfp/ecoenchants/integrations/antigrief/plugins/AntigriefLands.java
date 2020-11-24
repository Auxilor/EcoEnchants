package com.willfp.ecoenchants.integrations.antigrief.plugins;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefWrapper;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AntigriefLands implements AntigriefWrapper {
    @Override
    public boolean canBreakBlock(Player player, Block block) {
        LandsIntegration landsIntegration = new LandsIntegration(EcoEnchantsPlugin.getInstance());
        Area area = landsIntegration.getAreaByLoc(block.getLocation());
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_BREAK, false);
        }
        return true;
    }

    @Override
    public boolean canCreateExplosion(Player player, Location location) {
        LandsIntegration landsIntegration = new LandsIntegration(EcoEnchantsPlugin.getInstance());
        Area area = landsIntegration.getAreaByLoc(location);
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_IGNITE, false);
        }
        return true;
    }

    @Override
    public boolean canPlaceBlock(Player player, Block block) {
        LandsIntegration landsIntegration = new LandsIntegration(EcoEnchantsPlugin.getInstance());
        Area area = landsIntegration.getAreaByLoc(block.getLocation());
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_PLACE, false);
        }
        return true;
    }

    @Override
    public boolean canInjure(Player player, LivingEntity victim) {
        LandsIntegration landsIntegration = new LandsIntegration(EcoEnchantsPlugin.getInstance());
        Area area = landsIntegration.getAreaByLoc(victim.getLocation());
        if(victim instanceof Player) {
            if (area != null) {
                return area.canSetting(player, RoleSetting.ATTACK_PLAYER, false);
            }
        } else {
            if (area != null) {
                return area.canSetting(player, RoleSetting.ATTACK_ANIMAL, false);
            }
        }
        return true;
    }

    @Override
    public String getPluginName() {
        return "Lands";
    }
}

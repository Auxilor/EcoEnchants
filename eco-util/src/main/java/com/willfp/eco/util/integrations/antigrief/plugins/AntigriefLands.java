package com.willfp.eco.util.integrations.antigrief.plugins;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.integrations.antigrief.AntigriefWrapper;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AntigriefLands extends PluginDependent implements AntigriefWrapper {
    private final LandsIntegration landsIntegration = new LandsIntegration(this.getPlugin());

    public AntigriefLands(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean canBreakBlock(Player player, Block block) {
        Area area = landsIntegration.getAreaByLoc(block.getLocation());
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_BREAK, false);
        }
        return true;
    }

    @Override
    public boolean canCreateExplosion(Player player, Location location) {
        Area area = landsIntegration.getAreaByLoc(location);
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_IGNITE, false);
        }
        return true;
    }

    @Override
    public boolean canPlaceBlock(Player player, Block block) {
        Area area = landsIntegration.getAreaByLoc(block.getLocation());
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_PLACE, false);
        }
        return true;
    }

    @Override
    public boolean canInjure(Player player, LivingEntity victim) {
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

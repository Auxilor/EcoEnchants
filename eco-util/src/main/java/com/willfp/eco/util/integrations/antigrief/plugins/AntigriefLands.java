package com.willfp.eco.util.integrations.antigrief.plugins;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.integrations.antigrief.AntigriefWrapper;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AntigriefLands extends PluginDependent implements AntigriefWrapper {
    /**
     * Lands integration.
     */
    private final LandsIntegration landsIntegration = new LandsIntegration(this.getPlugin());

    /**
     * Instantiate new lands integration.
     *
     * @param plugin The integration provider.
     */
    public AntigriefLands(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean canBreakBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        Area area = landsIntegration.getAreaByLoc(block.getLocation());
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_BREAK, false);
        }
        return true;
    }

    @Override
    public boolean canCreateExplosion(@NotNull final Player player,
                                      @NotNull final Location location) {
        Area area = landsIntegration.getAreaByLoc(location);
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_IGNITE, false);
        }
        return true;
    }

    @Override
    public boolean canPlaceBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        Area area = landsIntegration.getAreaByLoc(block.getLocation());
        if (area != null) {
            return area.canSetting(player, RoleSetting.BLOCK_PLACE, false);
        }
        return true;
    }

    @Override
    public boolean canInjure(@NotNull final Player player,
                             @NotNull final LivingEntity victim) {
        Area area = landsIntegration.getAreaByLoc(victim.getLocation());
        if (victim instanceof Player) {
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

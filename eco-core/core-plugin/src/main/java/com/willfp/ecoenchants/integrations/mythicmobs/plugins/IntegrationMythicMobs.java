package com.willfp.ecoenchants.integrations.mythicmobs.plugins;

import com.willfp.ecoenchants.integrations.mythicmobs.MythicMobsWrapper;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class IntegrationMythicMobs implements MythicMobsWrapper {
    @Override
    public boolean canDropItems(@NotNull final Entity entity) {
        if (!MythicBukkit.inst().getAPIHelper().isMythicMob(entity)) {
            return true;
        }

        return !MythicBukkit.inst().getAPIHelper().getMythicMobInstance(entity).getType().getPreventMobKillDrops()
                && !MythicBukkit.inst().getAPIHelper().getMythicMobInstance(entity).getType().getPreventOtherDrops();
    }

    @Override
    public String getPluginName() {
        return "MythicMobs";
    }
}

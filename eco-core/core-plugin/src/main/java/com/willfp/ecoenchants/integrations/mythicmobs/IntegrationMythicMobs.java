package com.willfp.ecoenchants.integrations.mythicmobs;

import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.entity.Entity;

public class IntegrationMythicMobs {

    /**
     * True if MythicMobs integration is enabled.
     */
    private static boolean enabled = false;

    /**
     * Initiate MythicMobs integration.
     */
    public static void init() {
        enabled = true;
    }

    /**
     *
     * Check if given entity should drop items or not.
     *
     * @param entity - The entity to check.
     * @return - If given entity can drop items or not.
     */
    public static boolean canDropItems(Entity entity) {

        if (!enabled) return true;

        if (!MythicMobs.inst().getAPIHelper().isMythicMob(entity)) return true;

        return !MythicMobs.inst().getAPIHelper().getMythicMobInstance(entity).getType().getPreventMobKillDrops()
                && !MythicMobs.inst().getAPIHelper().getMythicMobInstance(entity).getType().getPreventOtherDrops();

    }

}

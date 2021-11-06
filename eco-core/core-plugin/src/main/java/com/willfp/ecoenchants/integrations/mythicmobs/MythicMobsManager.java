package com.willfp.ecoenchants.integrations.mythicmobs;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class MythicMobsManager {
    /**
     * All registered MythicMobs integrations.
     */
    private static final Set<MythicMobsWrapper> REGISTERED = new HashSet<>();

    /**
     * Register a new MythicMobs integration.
     *
     * @param integration The integration to register.
     */
    public static void register(@NotNull final MythicMobsWrapper integration) {
        REGISTERED.add(integration);
    }

    /**
     * Check if given entity should drop items or not.
     *
     * @param entity - The entity to check.
     * @return - If given entity can drop items or not.
     */
    public static boolean canDropItems(@NotNull final Entity entity) {
        for (MythicMobsWrapper wrapper : REGISTERED) {
            if (!wrapper.canDropItems(entity)) {
                return false;
            }
        }

        return true;
    }
}

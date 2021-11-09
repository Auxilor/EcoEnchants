package com.willfp.ecoenchants.integrations.mythicmobs;

import com.willfp.eco.core.integrations.Integration;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface MythicMobsWrapper extends Integration {
    /**
     * If an entity can drop items.
     *
     * @param entity The entity.
     * @return If can drop items.
     * @see MythicMobsManager#canDropItems(Entity)
     */
    boolean canDropItems(@NotNull Entity entity);
}


package com.willfp.eco.util.drops.internal;

import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.config.annotations.ConfigUpdater;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class DropManager {
    /**
     * The currently used type, or implementation, of {@link AbstractDropQueue}.
     * <p>
     * Standard by default, used if drops.collate key is not present in config.
     */
    @Getter
    private DropQueueType type = DropQueueType.STANDARD;

    /**
     * Update the type of drop queue that should be used.
     *
     * @see DropQueueType
     */
    @ConfigUpdater
    public void update() {
        type = Configs.CONFIG.getBool("drops.collate") ? DropQueueType.COLLATED : DropQueueType.STANDARD;
    }
}

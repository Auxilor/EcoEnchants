package com.willfp.eco.util.drops.internal;

import com.willfp.eco.util.config.Configs;
import lombok.Getter;

public final class DropManager {
    /**
     * The currently used type, or implementation, of {@link AbstractDropQueue}.
     * <p>
     * Standard by default, used if drops.collate key is not present in config.
     */
    @Getter
    private static DropQueueType type = DropQueueType.STANDARD;

    /**
     * Update the type of drop queue that should be used.
     *
     * @see DropQueueType
     */
    public static void update() {
        type = Configs.CONFIG.getBool("drops.collate") ? DropQueueType.COLLATED : DropQueueType.STANDARD;
    }

    static {
        update();
    }

    private DropManager() {

    }
}

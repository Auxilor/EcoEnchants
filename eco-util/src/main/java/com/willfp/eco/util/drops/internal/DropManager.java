package com.willfp.eco.util.drops.internal;

import com.willfp.eco.util.config.Configs;

public final class DropManager {
    /**
     * The currently used type.
     * <p>
     * Standard by default, used if drops.collate key is not present in config.
     */
    private static DropQueueType type = DropQueueType.STANDARD;

    /**
     * Get the type of {@link AbstractDropQueue} that should be used.
     *
     * @return The chosen {@link DropQueueType}.
     */
    public static DropQueueType getType() {
        return type;
    }

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

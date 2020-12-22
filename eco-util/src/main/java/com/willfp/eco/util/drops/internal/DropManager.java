package com.willfp.eco.util.drops.internal;

import com.willfp.eco.util.config.Configs;

public class DropManager {
    private static DropQueueType type = DropQueueType.STANDARD;

    public static DropQueueType getType() {
        return type;
    }

    public static void update() {
        type = Configs.CONFIG.getBool("drops.collate") ? DropQueueType.COLLATED : DropQueueType.STANDARD;
    }

    static {
        update();
    }
}

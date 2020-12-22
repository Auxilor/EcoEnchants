package com.willfp.eco.util.drops.internal;

public enum DropQueueType {
    /**
     * As drops are processed, push them to the world or to the player's inventory.
     */
    STANDARD,

    /**
     * As drops are processed, add them to a queue that are all collectively pushed at the end of a tick.
     * <p>
     * Generally better performance.
     */
    COLLATED
}

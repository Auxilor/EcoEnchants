package com.willfp.eco.util.config.annotations;

import org.jetbrains.annotations.NotNull;

public class InvalidUpdatableClassException extends RuntimeException {
    /**
     * Called when an updatable class is registered into an {@link com.willfp.eco.util.config.ConfigHandler}.
     *
     * @param message The error message.
     */
    public InvalidUpdatableClassException(@NotNull final String message) {
        super(message);
    }
}

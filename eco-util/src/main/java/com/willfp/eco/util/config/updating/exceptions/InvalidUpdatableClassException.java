package com.willfp.eco.util.config.updating.exceptions;

import com.willfp.eco.util.config.updating.ConfigHandler;
import org.jetbrains.annotations.NotNull;

public class InvalidUpdatableClassException extends RuntimeException {
    /**
     * Called when an updatable class is registered into an {@link ConfigHandler}.
     *
     * @param message The error message.
     */
    public InvalidUpdatableClassException(@NotNull final String message) {
        super(message);
    }
}

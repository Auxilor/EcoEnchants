package com.willfp.eco.core.proxy;

import org.jetbrains.annotations.NotNull;

public class UnsupportedVersionException extends RuntimeException {
    /**
     * Thrown if the server is running an unsupported NMS version.
     *
     * @param message The message to send.
     */
    public UnsupportedVersionException(@NotNull final String message) {
        super(message);
    }
}

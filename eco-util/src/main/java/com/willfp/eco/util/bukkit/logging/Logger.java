package com.willfp.eco.util.bukkit.logging;

public interface Logger {
    /**
     * Log an informative/neutral message to console.
     *
     * @param message The message to log.
     */
    void info(String message);

    /**
     * Log a warning to console.
     *
     * @param message The message to log.
     */
    void warn(String message);

    /**
     * Log an error to console.
     *
     * @param message The message to log.
     */
    void error(String message);
}

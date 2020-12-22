package com.willfp.eco.util.integrations;

/**
 * Interface for all integrations with optional dependencies
 */
public interface Integration {
    /**
     * Get the name of integration
     * @return The name
     */
    String getPluginName();
}

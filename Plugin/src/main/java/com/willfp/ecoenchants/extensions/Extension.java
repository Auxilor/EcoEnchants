package com.willfp.ecoenchants.extensions;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Extensions are a way of interfacing with EcoEnchants
 * Syntactically similar to Bukkit Plugins.
 */
public abstract class Extension {
    /**
     * Metadata containing version and name
     */
    private ExtensionMetadata metadata = null;

    /**
     * Method to validate metadata and enable extension
     */
    public final void enable() {
        Validate.notNull(metadata, "Metadata cannot be null!");
        this.onEnable();
    }

    /**
     * Method to disable extension
     */
    public final void disable() {
        this.onDisable();
    }

    /**
     * Called on enabling Extension
     */
    protected abstract void onEnable();

    /**
     * Called when Extension is disabled
     */
    protected abstract void onDisable();

    /**
     * Set the metadata of the extension
     *
     * Must be called before enabling
     * @param metadata The metadata to set
     */
    public final void setMetadata(ExtensionMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the name of the extension
     * @return The name of the metadata attached to the extension
     */
    public final String getName() {
        Validate.notNull(metadata, "Metadata cannot be null!");
        return this.metadata.name;
    }

    /**
     * Get the version of the extension
     * @return The version of the metadata attached to the extension
     */
    public final String getVersion() {
        Validate.notNull(metadata, "Metadata cannot be null!");
        return this.metadata.version;
    }

    /**
     * Wrapper for the string and version of the extension
     * Contains versions and name
     * Designed for internal use
     */
    @ApiStatus.Internal
    public static final class ExtensionMetadata {
        private final @NotNull String version;
        private final @NotNull String name;

        public ExtensionMetadata(@NotNull String version, @NotNull String name) {
            this.version = version;
            this.name = name;
        }
    }
}

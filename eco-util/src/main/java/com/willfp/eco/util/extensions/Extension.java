package com.willfp.eco.util.extensions;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public abstract class Extension {
    /**
     * The {@link AbstractEcoPlugin} that this extension is for.
     */
    @Getter(AccessLevel.PROTECTED)
    private final AbstractEcoPlugin plugin = AbstractEcoPlugin.getInstance();

    /**
     * Metadata containing version and name.
     */
    private ExtensionMetadata metadata = null;

    /**
     * Method to validate metadata and enable extension.
     */
    public final void enable() {
        Validate.notNull(metadata, "Metadata cannot be null!");
        this.onEnable();
    }

    /**
     * Method to disable extension.
     */
    public final void disable() {
        this.onDisable();
    }

    /**
     * Called on enabling Extension.
     */
    protected abstract void onEnable();

    /**
     * Called when Extension is disabled.
     */
    protected abstract void onDisable();

    /**
     * Set the metadata of the extension.
     * <p>
     * Must be called before enabling.
     *
     * @param metadata The metadata to set.
     */
    public final void setMetadata(@NotNull final ExtensionMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the name of the extension.
     *
     * @return The name of the metadata attached to the extension.
     */
    public final String getName() {
        Validate.notNull(metadata, "Metadata cannot be null!");
        return this.metadata.name;
    }

    /**
     * Get the version of the extension.
     *
     * @return The version of the metadata attached to the extension.
     */
    public final String getVersion() {
        Validate.notNull(metadata, "Metadata cannot be null!");
        return this.metadata.version;
    }

    /**
     * Wrapper for the string and version of the extension.
     * Contains versions and name.
     * Designed for internal use.
     */
    @ApiStatus.Internal
    public static final class ExtensionMetadata {
        /**
         * The version of the extension.
         */
        private final @NotNull String version;

        /**
         * The extension's name.
         */
        private final @NotNull String name;

        /**
         * Create a new extension metadata.
         *
         * @param version The version for the extension to be.
         * @param name    The name of the extension.
         */
        public ExtensionMetadata(@NotNull final String version,
                                 @NotNull final String name) {
            this.version = version;
            this.name = name;
        }
    }
}

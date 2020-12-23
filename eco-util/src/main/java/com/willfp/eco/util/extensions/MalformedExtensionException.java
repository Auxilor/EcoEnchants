package com.willfp.eco.util.extensions;

/**
 * Called when the extension is made incorrectly.
 */
public class MalformedExtensionException extends RuntimeException {

    /**
     * Create a new MalformedExtensionException.
     * <p>
     * Potential causes include:
     * Missing or invalid extension.yml.
     * Invalid filetype.
     *
     * @param errorMessage The error message to show.
     */
    public MalformedExtensionException(String errorMessage) {
        super(errorMessage);
    }
}

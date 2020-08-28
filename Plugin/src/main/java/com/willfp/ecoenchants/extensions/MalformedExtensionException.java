package com.willfp.ecoenchants.extensions;

/**
 * Called when the extension is made incorrectly
 */
public class MalformedExtensionException extends RuntimeException {
    public MalformedExtensionException(String errorMessage) {
        super(errorMessage);
    }
}

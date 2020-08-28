package com.willfp.ecoenchants.enchantments;

/**
 * Triggered if enchantment is invalid (for extensions)
 */
public class InvalidEnchantmentException extends RuntimeException {
    public InvalidEnchantmentException(String errorMessage) {
        super(errorMessage);
    }
}

package com.willfp.ecoenchants.enchantments.util;

import java.io.InputStream;

/**
 * Exists to simplify enchantment registration
 *
 * Prevents requiring direct class referencing on the front-end of enchantment creation
 *
 * @see com.willfp.ecoenchants.extensions.Extension
 */
public interface EnchantmentRegisterer {
    default InputStream getResourceAsStream(String location) {
        return this.getClass().getResourceAsStream(location);
    }
}

package com.willfp.ecoenchants.integrations.essentials;

import com.willfp.ecoenchants.integrations.Integration;

/**
 * Interface for Essentials Integration
 */
public interface EssentialsWrapper extends Integration {
    /**
     * @see EssentialsManager#registerEnchantments() 
     */
    void registerAllEnchantments();
}

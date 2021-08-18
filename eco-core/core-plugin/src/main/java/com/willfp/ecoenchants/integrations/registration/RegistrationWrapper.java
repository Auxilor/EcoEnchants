package com.willfp.ecoenchants.integrations.registration;

import com.willfp.eco.core.integrations.Integration;

public interface RegistrationWrapper extends Integration {
    /**
     * @see RegistrationManager#registerEnchantments();
     */
    void registerAllEnchantments();
}


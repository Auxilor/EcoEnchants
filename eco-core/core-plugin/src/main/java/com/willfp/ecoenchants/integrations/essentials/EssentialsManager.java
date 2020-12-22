package com.willfp.ecoenchants.integrations.essentials;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for interfacing with EssentialsX
 */
public class EssentialsManager {
    private static final Set<EssentialsWrapper> registered = new HashSet<>();

    /**
     * Register a new essentials integration
     *
     * @param essentials The integration to register
     */
    public static void register(EssentialsWrapper essentials) {
        registered.add(essentials);
    }

    /**
     * Register all {@link com.willfp.ecoenchants.enchantments.EcoEnchant}s with Essentials
     */
    public static void registerEnchantments() {
        registered.forEach((EssentialsWrapper::registerAllEnchantments));
    }
}

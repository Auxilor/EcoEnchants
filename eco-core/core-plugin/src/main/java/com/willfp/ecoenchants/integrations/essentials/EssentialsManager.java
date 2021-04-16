package com.willfp.ecoenchants.integrations.essentials;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class EssentialsManager {
    /**
     * All registered essentials integrations.
     */
    private static final Set<EssentialsWrapper> REGISTERED = new HashSet<>();

    /**
     * Register a new essentials integration.
     *
     * @param essentials The integration to register.
     */
    public static void register(@NotNull final EssentialsWrapper essentials) {
        REGISTERED.add(essentials);
    }

    /**
     * Register all {@link com.willfp.ecoenchants.enchantments.EcoEnchant}s with Essentials.
     */
    public static void registerEnchantments() {
        REGISTERED.forEach(EssentialsWrapper::registerAllEnchantments);
    }
}

package com.willfp.ecoenchants.integrations.registration;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class RegistrationManager {
    /**
     * All registered essentials integrations.
     */
    private static final Set<RegistrationWrapper> REGISTERED = new HashSet<>();

    /**
     * Register a new essentials integration.
     *
     * @param essentials The integration to register.
     */
    public static void register(@NotNull final RegistrationWrapper essentials) {
        REGISTERED.add(essentials);
    }

    /**
     * Register all {@link com.willfp.ecoenchants.enchantments.EcoEnchant}s with Essentials.
     */
    public static void registerEnchantments() {
        for (RegistrationWrapper wrapper : REGISTERED) {
            try {
                wrapper.registerAllEnchantments();
            } catch (Exception e) {
                Bukkit.getLogger().warning("Failed to register enchantments for plugin " + wrapper.getPluginName());
            }
        }
    }
}

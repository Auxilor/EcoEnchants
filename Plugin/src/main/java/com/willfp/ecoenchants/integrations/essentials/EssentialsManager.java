package com.willfp.ecoenchants.integrations.essentials;

import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;

public class EssentialsManager {
    private static final Set<EssentialsWrapper> registered = new HashSet<>();

    public static boolean registerIfPresent(EssentialsWrapper essentials) {
        if(Bukkit.getPluginManager().isPluginEnabled(essentials.getPluginName())) {
            registered.add(essentials);
            return true;
        }
        return false;
    }

    public static void registerEnchantments() {
        registered.forEach((EssentialsWrapper::registerAllEnchantments));
    }
}

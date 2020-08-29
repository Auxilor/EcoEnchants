package com.willfp.ecoenchants.integrations.essentials;

import java.util.HashSet;
import java.util.Set;

public class EssentialsManager {
    private static final Set<EssentialsWrapper> registered = new HashSet<>();

    public static void registerAntigrief(EssentialsWrapper essentials) {
        registered.add(essentials);
    }

    public static void registerEnchantments() {
        registered.forEach((EssentialsWrapper::registerAllEnchantments));
    }
}

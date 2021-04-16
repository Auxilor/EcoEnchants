package com.willfp.ecoenchants.firewand;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class FirewandMain extends Extension {
    public static final EcoEnchant FIREWAND = new Firewand();

    public FirewandMain(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        // Handled by super
    }

    @Override
    public void onDisable() {
        // Handled by super
    }
}

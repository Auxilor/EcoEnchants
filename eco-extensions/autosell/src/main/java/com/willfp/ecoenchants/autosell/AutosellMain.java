package com.willfp.ecoenchants.autosell;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class AutosellMain extends Extension {
    /**
     * Autosell enchantment.
     */
    public static final EcoEnchant AUTOSELL = new Autosell();

    public AutosellMain(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        EconomyHandler.setEnabled(EconomyHandler.init());
    }

    @Override
    public void onDisable() {
        // Handled by super
    }
}

package com.willfp.ecoenchants.alchemy;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class AlchemyMain extends Extension {
    /**
     * Alchemy enchantment.
     */
    public static final EcoEnchant ALCHEMY = new Alchemy();

    public AlchemyMain(@NotNull final EcoPlugin plugin) {
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

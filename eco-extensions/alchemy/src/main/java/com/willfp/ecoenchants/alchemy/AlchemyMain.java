package com.willfp.ecoenchants.alchemy;

import com.willfp.eco.util.extensions.Extension;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class AlchemyMain extends Extension {
    /**
     * Alchemy enchantment.
     */
    public static final EcoEnchant ALCHEMY = new Alchemy();

    public AlchemyMain(@NotNull final AbstractEcoPlugin plugin) {
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

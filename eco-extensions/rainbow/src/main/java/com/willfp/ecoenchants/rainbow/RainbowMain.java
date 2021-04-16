package com.willfp.ecoenchants.rainbow;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class RainbowMain extends Extension {
    public static final EcoEnchant RAINBOW = new Rainbow();

    public RainbowMain(@NotNull final EcoPlugin plugin) {
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

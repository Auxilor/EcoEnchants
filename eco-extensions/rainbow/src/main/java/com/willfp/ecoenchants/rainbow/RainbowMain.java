package com.willfp.ecoenchants.rainbow;

import com.willfp.eco.util.extensions.Extension;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class RainbowMain extends Extension {
    public static final EcoEnchant RAINBOW = new Rainbow();

    public RainbowMain(@NotNull final AbstractEcoPlugin plugin) {
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

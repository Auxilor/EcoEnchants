package com.willfp.ecoenchants.endershot;


import com.willfp.eco.util.extensions.Extension;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class EndershotMain extends Extension {
    public static final EcoEnchant ENDERSHOT = new Endershot();

    public EndershotMain(@NotNull final AbstractEcoPlugin plugin) {
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

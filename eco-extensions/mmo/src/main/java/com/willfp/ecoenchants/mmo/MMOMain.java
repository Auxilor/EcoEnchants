package com.willfp.ecoenchants.mmo;

import com.willfp.eco.util.extensions.Extension;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class MMOMain extends Extension {
    public static final EcoEnchant ELIXIR = new Elixir();
    public static final EcoEnchant SIPHON = new Siphon();
    public static final EcoEnchant DRAIN = new Drain();
    public static final EcoEnchant SPIRITUALITY = new Spirituality();
    public static final EcoEnchant AUGMENT = new Augment();
    public static final EcoEnchant DISCOUNTED = new Discounted();
    public static final EcoEnchant RECOVER = new Recover();
    public static final EcoEnchant ENDURANCE = new Endurance();
    public static final EcoEnchant FORTITUDE = new Fortitude();
    public static final EcoEnchant MOTIVATE = new Motivate();
    public static final EcoEnchant ATHLETIC = new Athletic();
    public static final EcoEnchant STRENGTHENING = new Strengthening();

    public MMOMain(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onEnable() {
        if (!MMOPrerequisites.HAS_MMOCORE.isMet()) {
            Bukkit.getLogger().severe("MMO Extension requires MMOCore to be installed!");
            Bukkit.getLogger().severe("Disabling...");
            this.disable();
        }
    }

    @Override
    protected void onDisable() {
        // Handled by super
    }
}

package com.willfp.ecoenchants.mmo;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.mmo.enchants.abilities.Discounted;
import com.willfp.ecoenchants.mmo.enchants.abilities.Recover;
import com.willfp.ecoenchants.mmo.enchants.mana.Augment;
import com.willfp.ecoenchants.mmo.enchants.mana.Drain;
import com.willfp.ecoenchants.mmo.enchants.mana.Elixir;
import com.willfp.ecoenchants.mmo.enchants.mana.Siphon;
import com.willfp.ecoenchants.mmo.enchants.mana.Spirituality;
import com.willfp.ecoenchants.mmo.enchants.misc.Strengthening;
import com.willfp.ecoenchants.mmo.enchants.stamina.Athletic;
import com.willfp.ecoenchants.mmo.enchants.stamina.Endurance;
import com.willfp.ecoenchants.mmo.enchants.stamina.Fortitude;
import com.willfp.ecoenchants.mmo.enchants.stamina.Motivate;
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

    public MMOMain(@NotNull final EcoPlugin plugin) {
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

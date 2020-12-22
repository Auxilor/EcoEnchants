package com.willfp.ecoenchants.mmo;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.extensions.Extension;
import com.willfp.ecoenchants.mmo.enchants.misc.Strengthening;
import com.willfp.ecoenchants.mmo.enchants.stamina.Athletic;
import com.willfp.ecoenchants.mmo.enchants.mana.Augment;
import com.willfp.ecoenchants.mmo.enchants.abilities.Discounted;
import com.willfp.ecoenchants.mmo.enchants.abilities.Recover;
import com.willfp.ecoenchants.mmo.enchants.mana.Drain;
import com.willfp.ecoenchants.mmo.enchants.mana.Elixir;
import com.willfp.ecoenchants.mmo.enchants.mana.Siphon;
import com.willfp.ecoenchants.mmo.enchants.mana.Spirituality;
import com.willfp.ecoenchants.mmo.enchants.stamina.Endurance;
import com.willfp.ecoenchants.mmo.enchants.stamina.Fortitude;
import com.willfp.ecoenchants.mmo.enchants.stamina.Motivate;
import com.willfp.ecoenchants.mmo.structure.MMOEnchant;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.Bukkit;

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

    @Override
    protected void onEnable() {
        if(!MMOPrerequisites.HAS_MMOCORE.isMet()) {
            Logger.error("MMO Extension requires MMOCore to be installed!");
            Logger.error("Disabling...");
            this.disable();
        }

        MMOEnchant.REGISTRY.forEach(mmoEnchant -> {
            Bukkit.getPluginManager().registerEvents((EcoEnchant) mmoEnchant, this.plugin);
        });
    }

    @Override
    protected void onDisable() {

    }
}

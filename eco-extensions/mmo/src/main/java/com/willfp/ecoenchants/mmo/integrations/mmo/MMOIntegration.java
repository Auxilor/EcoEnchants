package com.willfp.ecoenchants.mmo.integrations.mmo;


import com.willfp.eco.core.integrations.Integration;
import org.bukkit.entity.Player;

public interface MMOIntegration extends Integration {
    double getMana(Player player);

    void setMana(Player player, double amount);

    double getMaxMana(Player player);

    void giveMana(Player player, double amount);

    double getStamina(Player player);

    void setStamina(Player player, double amount);

    double getMaxStamina(Player player);

    void giveStamina(Player player, double amount);
}

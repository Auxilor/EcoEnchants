package com.willfp.ecoenchants.mmo.integrations.mmo;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class MMOManager {
    private static final Set<MMOIntegration> integrations = new HashSet<>();

    public static void register(MMOIntegration integration) {
        integrations.add(integration);
    }

    private static MMOIntegration getIntegration() {
        Validate.notEmpty(integrations, "There must be an existing mmo integration!");
        return integrations.stream().findFirst().get();
    }

    public static double getMana(Player player) {
        MMOIntegration integration = getIntegration();
        return integration.getMana(player);
    }

    public static double getMaxMana(Player player) {
        MMOIntegration integration = getIntegration();
        return integration.getMaxMana(player);
    }

    public static void setMana(Player player, double amount) {
        MMOIntegration integration = getIntegration();
        integration.setMana(player, amount);
    }

    public static void giveMana(Player player, double amount) {
        MMOIntegration integration = getIntegration();
        integration.giveMana(player, amount);
    }

    public static double getStamina(Player player) {
        MMOIntegration integration = getIntegration();
        return integration.getMana(player);
    }

    public static double getMaxStamina(Player player) {
        MMOIntegration integration = getIntegration();
        return integration.getMaxMana(player);
    }

    public static void setStamina(Player player, double amount) {
        MMOIntegration integration = getIntegration();
        integration.setMana(player, amount);
    }

    public static void giveStamina(Player player, double amount) {
        MMOIntegration integration = getIntegration();
        integration.giveMana(player, amount);
    }
}

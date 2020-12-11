package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.CooldownWrapper;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Utility class to get the attack cooldown of a player
 */
public class Cooldown {
    private static CooldownWrapper cooldown;

    /**
     * Get a player's attack cooldown
     *
     * @param player The player to check
     * @return A value between 0 and 1, with 1 representing max strength
     */
    public static double getCooldown(Player player) {
        return cooldown.getAttackCooldown(player);
    }

    static {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + EcoEnchantsPlugin.NMS_VERSION + ".Cooldown");
            if (CooldownWrapper.class.isAssignableFrom(class2)) {
                cooldown = (CooldownWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            Logger.error("&cYou're running an unsupported server version: " + EcoEnchantsPlugin.NMS_VERSION);
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
    }
}

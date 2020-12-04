package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.CooldownWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Utility class to get the attack cooldown of a player
 */
public class Cooldown {
    private static CooldownWrapper cooldown;

    @ApiStatus.Internal
    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + EcoEnchantsPlugin.NMS_VERSION + ".Cooldown");
            if (CooldownWrapper.class.isAssignableFrom(class2)) {
                cooldown = (CooldownWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            cooldown = null;
        }
        return cooldown != null;
    }

    /**
     * Get a player's attack cooldown
     *
     * @param player The player to check
     * @return A value between 0 and 1, with 1 representing max strength
     */
    public static double getCooldown(Player player) {
        assert cooldown != null;
        return cooldown.getAttackCooldown(player);
    }
}

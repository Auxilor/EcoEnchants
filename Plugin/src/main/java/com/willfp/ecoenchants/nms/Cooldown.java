package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.nms.API.CooldownWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Cooldown {
    private static CooldownWrapper cooldown;

    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + version + ".Cooldown");
            if (CooldownWrapper.class.isAssignableFrom(class2)) {
                cooldown = (CooldownWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            cooldown = null;
        }
        return cooldown != null;
    }

    public static double getCooldown(Player player) {
        assert cooldown != null;
        return cooldown.getAttackCooldown(player);
    }
}

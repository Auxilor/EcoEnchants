package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.OpenInventoryWrapper;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Utility class to get the NMS implementation of a players' currently open inventory
 */
public class OpenInventory {
    private static OpenInventoryWrapper openInventoryWrapper;

    /**
     * Get the NMS container of the inventory
     *
     * @param player The player to check
     * @return The NMS container
     */
    public static Object getOpenInventory(Player player) {
        return openInventoryWrapper.getOpenInventory(player);
    }

    static {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants.nms." + EcoEnchantsPlugin.NMS_VERSION + ".OpenInventory");
            if (OpenInventoryWrapper.class.isAssignableFrom(class2)) {
                openInventoryWrapper = (OpenInventoryWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            Logger.error("&cYou're running an unsupported server version: " + EcoEnchantsPlugin.NMS_VERSION);
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
    }
}

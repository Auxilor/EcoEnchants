package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.OpenInventoryWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Utility class to get the NMS implementation of a players' currently open inventory
 */
public class OpenInventory {
    private static OpenInventoryWrapper openInventoryWrapper;

    @ApiStatus.Internal
    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + EcoEnchantsPlugin.NMS_VERSION + ".OpenInventory");
            if (OpenInventoryWrapper.class.isAssignableFrom(class2)) {
                openInventoryWrapper = (OpenInventoryWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            openInventoryWrapper = null;
        }
        return openInventoryWrapper != null;
    }

    /**
     * Get the NMS container of the inventory
     *
     * @param player The player to check
     * @return The NMS container
     */
    public static Object getOpenInventory(Player player) {
        assert openInventoryWrapper != null;
        return openInventoryWrapper.getOpenInventory(player);
    }
}

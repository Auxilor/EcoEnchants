package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.API.BlockBreakWrapper;
import com.willfp.ecoenchants.nms.API.OpenInventoryWrapper;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class OpenInventory {
    private static OpenInventoryWrapper openInventoryWrapper;

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

    public static Object getOpenInventory(Player player) {
        assert openInventoryWrapper != null;
        return openInventoryWrapper.getOpenInventory(player);
    }
}

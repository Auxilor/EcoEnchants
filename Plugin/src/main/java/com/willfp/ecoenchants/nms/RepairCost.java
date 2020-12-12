package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.RepairCostWrapper;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class to get and set the anvil rework penalty of an item
 */
public class RepairCost {
    private static RepairCostWrapper repairCostWrapper;

    /**
     * Get the rework penalty of an ItemStack
     *
     * @param itemStack The item to check
     *
     * @return The anvil rework penalty
     */
    public static int getRepairCost(ItemStack itemStack) {
        return repairCostWrapper.getRepairCost(itemStack);
    }

    /**
     * Set the rework penalty of an ItemStack
     *
     * @param itemStack The item to set
     * @param cost      The penalty to set
     *
     * @return The ItemStack, with the repair cost set
     */
    public static ItemStack setRepairCost(ItemStack itemStack, int cost) {
        return repairCostWrapper.setRepairCost(itemStack, cost);
    }

    static {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants.nms." + EcoEnchantsPlugin.NMS_VERSION + ".RepairCost");
            if (RepairCostWrapper.class.isAssignableFrom(class2)) {
                repairCostWrapper = (RepairCostWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            Logger.error("&cYou're running an unsupported server version: " + EcoEnchantsPlugin.NMS_VERSION);
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
    }
}

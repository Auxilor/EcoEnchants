package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.API.RepairCostWrapper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * Utility class to get and set the anvil rework penalty of an item
 */
public class RepairCost {
    private static RepairCostWrapper repairCostWrapper;

    @ApiStatus.Internal
    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + EcoEnchantsPlugin.NMS_VERSION + ".RepairCost");
            if (RepairCostWrapper.class.isAssignableFrom(class2)) {
                repairCostWrapper = (RepairCostWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            repairCostWrapper = null;
        }
        return repairCostWrapper != null;
    }

    /**
     * Get the rework penalty of an ItemStack
     *
     * @param itemStack The item to check
     * @return The anvil rework penalty
     */
    public static int getRepairCost(ItemStack itemStack) {
        assert repairCostWrapper != null;
        return repairCostWrapper.getRepairCost(itemStack);
    }

    /**
     * Set the rework penalty of an ItemStack
     *
     * @param itemStack The item to set
     * @param cost      The penalty to set
     * @return The ItemStack, with the repair cost set
     */
    public static ItemStack setRepairCost(ItemStack itemStack, int cost) {
        assert repairCostWrapper != null;
        return repairCostWrapper.setRepairCost(itemStack, cost);
    }
}

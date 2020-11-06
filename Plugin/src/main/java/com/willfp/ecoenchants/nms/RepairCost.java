package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.API.RepairCostWrapper;
import org.bukkit.inventory.ItemStack;

public class RepairCost {
    private static RepairCostWrapper repairCostWrapper;

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

    public static int getRepairCost(ItemStack itemStack) {
        assert repairCostWrapper != null;
        return repairCostWrapper.getRepairCost(itemStack);
    }

    public static ItemStack setRepairCost(ItemStack itemStack, int cost) {
        assert repairCostWrapper != null;
        return repairCostWrapper.setRepairCost(itemStack, cost);
    }
}

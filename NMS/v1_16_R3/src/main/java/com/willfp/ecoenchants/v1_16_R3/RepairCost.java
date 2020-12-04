package com.willfp.ecoenchants.v1_16_R3;

import com.willfp.ecoenchants.nms.api.RepairCostWrapper;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class RepairCost implements RepairCostWrapper {
    @Override
    public ItemStack setRepairCost(ItemStack itemStack, int cost) {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        nmsStack.setRepairCost(cost);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public int getRepairCost(ItemStack itemStack) {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsStack.getRepairCost();
    }
}
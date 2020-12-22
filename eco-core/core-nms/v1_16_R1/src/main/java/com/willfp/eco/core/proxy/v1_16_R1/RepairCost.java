package com.willfp.eco.core.proxy.v1_16_R1;

import com.willfp.eco.core.proxy.proxies.RepairCostProxy;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class RepairCost implements RepairCostProxy {
    @Override
    public ItemStack setRepairCost(ItemStack itemStack, int cost) {
        net.minecraft.server.v1_16_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        nmsStack.setRepairCost(cost);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public int getRepairCost(ItemStack itemStack) {
        net.minecraft.server.v1_16_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsStack.getRepairCost();
    }
}
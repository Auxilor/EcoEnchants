package com.willfp.ecoenchants.v1_16_R2;

import com.willfp.ecoenchants.nms.API.RepairCostWrapper;
import com.willfp.ecoenchants.nms.API.TridentStackWrapper;
import net.minecraft.server.v1_16_R2.EntityThrownTrident;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.NBTTagInt;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftTrident;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

public class RepairCost implements RepairCostWrapper {
    @Override
    public ItemStack setRepairCost(ItemStack itemStack, int cost) {
        net.minecraft.server.v1_16_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        nmsStack.setRepairCost(cost);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public int getRepairCost(ItemStack itemStack) {
        net.minecraft.server.v1_16_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsStack.getRepairCost();
    }
}
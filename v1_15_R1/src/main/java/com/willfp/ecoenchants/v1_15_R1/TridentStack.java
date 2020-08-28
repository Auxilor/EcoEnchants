package com.willfp.ecoenchants.v1_15_R1;

import com.willfp.ecoenchants.API.TridentStackWrapper;
import net.minecraft.server.v1_15_R1.EntityThrownTrident;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftTrident;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

public class TridentStack implements TridentStackWrapper {
    @Override
    public ItemStack getTridentStack(Trident trident) {
        EntityThrownTrident t = ((CraftTrident) trident).getHandle();
        return CraftItemStack.asBukkitCopy(t.trident);
    }
}
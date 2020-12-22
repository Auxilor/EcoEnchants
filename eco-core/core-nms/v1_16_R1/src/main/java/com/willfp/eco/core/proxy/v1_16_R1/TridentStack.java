package com.willfp.eco.core.proxy.v1_16_R1;

import com.willfp.eco.core.proxy.proxies.TridentStackProxy;
import net.minecraft.server.v1_16_R1.EntityThrownTrident;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftTrident;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

public class TridentStack implements TridentStackProxy {
    @Override
    public ItemStack getTridentStack(Trident trident) {
        EntityThrownTrident t = ((CraftTrident) trident).getHandle();
        return CraftItemStack.asBukkitCopy(t.trident);
    }
}
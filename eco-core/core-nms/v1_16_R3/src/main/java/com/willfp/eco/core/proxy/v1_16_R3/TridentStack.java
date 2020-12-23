package com.willfp.eco.core.proxy.v1_16_R3;

import com.willfp.eco.core.proxy.proxies.TridentStackProxy;
import net.minecraft.server.v1_16_R3.EntityThrownTrident;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftTrident;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class TridentStack implements TridentStackProxy {
    @Override
    public ItemStack getTridentStack(@NotNull final Trident trident) {
        EntityThrownTrident t = ((CraftTrident) trident).getHandle();
        return CraftItemStack.asBukkitCopy(t.trident);
    }
}
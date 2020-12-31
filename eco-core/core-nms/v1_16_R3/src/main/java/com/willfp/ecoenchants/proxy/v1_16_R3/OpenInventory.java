package com.willfp.ecoenchants.proxy.v1_16_R3;

import com.willfp.ecoenchants.proxy.proxies.OpenInventoryProxy;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class OpenInventory implements OpenInventoryProxy {
    @Override
    public Object getOpenInventory(@NotNull final Player player) {
        return ((CraftPlayer) player).getHandle().activeContainer;
    }
}

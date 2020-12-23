package com.willfp.eco.core.proxy.v1_15_R1;

import com.willfp.eco.core.proxy.proxies.OpenInventoryProxy;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class OpenInventory implements OpenInventoryProxy {
    @Override
    public Object getOpenInventory(@NotNull final Player player) {
        return ((CraftPlayer) player).getHandle().activeContainer;
    }
}

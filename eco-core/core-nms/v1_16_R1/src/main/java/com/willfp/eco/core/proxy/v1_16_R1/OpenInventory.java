package com.willfp.eco.core.proxy.v1_16_R1;

import com.willfp.eco.core.proxy.proxies.OpenInventoryProxy;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class OpenInventory implements OpenInventoryProxy {
    @Override
    public Object getOpenInventory(Player player) {
        return ((CraftPlayer) player).getHandle().activeContainer;
    }
}

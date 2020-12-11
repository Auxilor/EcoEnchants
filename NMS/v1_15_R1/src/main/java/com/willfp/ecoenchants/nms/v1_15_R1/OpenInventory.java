package com.willfp.ecoenchants.nms.v1_15_R1;

import com.willfp.ecoenchants.nms.api.OpenInventoryWrapper;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class OpenInventory implements OpenInventoryWrapper {
    @Override
    public Object getOpenInventory(Player player) {
        return ((CraftPlayer) player).getHandle().activeContainer;
    }
}

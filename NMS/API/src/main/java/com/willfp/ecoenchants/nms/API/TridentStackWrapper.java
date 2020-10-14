package com.willfp.ecoenchants.nms.API;

import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

public interface TridentStackWrapper {
    ItemStack getTridentStack(Trident trident);
}
package com.willfp.ecoenchants.nms.API;

import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

/**
 * NMS Interface for getting an ItemStack from a Trident
 */
public interface TridentStackWrapper {
    ItemStack getTridentStack(Trident trident);
}
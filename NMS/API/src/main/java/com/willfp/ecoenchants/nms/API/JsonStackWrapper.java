package com.willfp.ecoenchants.nms.API;

import org.bukkit.inventory.ItemStack;

public interface JsonStackWrapper {
    ItemStack getFromTag(String jsonTag, String id);
    String toJson(ItemStack itemStack);
}

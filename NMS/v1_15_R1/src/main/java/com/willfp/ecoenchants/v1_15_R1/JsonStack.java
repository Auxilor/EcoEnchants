package com.willfp.ecoenchants.v1_15_R1;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.willfp.ecoenchants.nms.API.JsonStackWrapper;
import net.minecraft.server.v1_15_R1.MojangsonParser;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class JsonStack implements JsonStackWrapper {
    @Override
    public ItemStack getFromTag(String jsonTag, String id) {
        id = id.replaceAll("minecraft:", "").toUpperCase();
        Material material = Material.getMaterial(id);

        assert material != null;
        ItemStack itemStack = new ItemStack(material);
        net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

        try {
            nmsStack.setTag(MojangsonParser.parse(jsonTag));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public String toJson(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack).getOrCreateTag().toString();
    }
}

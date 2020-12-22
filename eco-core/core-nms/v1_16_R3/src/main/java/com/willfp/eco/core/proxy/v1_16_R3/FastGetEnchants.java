package com.willfp.eco.core.proxy.v1_16_R3;

import com.willfp.eco.core.proxy.proxies.FastGetEnchantsProxy;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FastGetEnchants implements FastGetEnchantsProxy {
    @Override
    public Map<Enchantment, Integer> getEnchantmentsOnItem(ItemStack itemStack) {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagList enchantmentNBT = nmsStack.getEnchantments();
        HashMap<Enchantment, Integer> foundEnchantments = new HashMap<>();

        for (NBTBase base : enchantmentNBT) {
            NBTTagCompound compound = (NBTTagCompound) base;
            String key = compound.getString("id");
            int level = '\uffff' & compound.getShort("lvl");

            Enchantment found = Enchantment.getByKey(CraftNamespacedKey.fromStringOrNull(key));
            if(found != null) foundEnchantments.put(found, level);
        }
        return foundEnchantments;
    }

    @Override
    public int getLevelOnItem(ItemStack itemStack, Enchantment enchantment) {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagList enchantmentNBT = nmsStack.getEnchantments();

        for (NBTBase base : enchantmentNBT) {
            NBTTagCompound compound = (NBTTagCompound) base;
            String key = compound.getString("id");
            if(!key.equals(enchantment.getKey().toString()))
                continue;

            return '\uffff' & compound.getShort("lvl");
        }
        return 0;
    }
}

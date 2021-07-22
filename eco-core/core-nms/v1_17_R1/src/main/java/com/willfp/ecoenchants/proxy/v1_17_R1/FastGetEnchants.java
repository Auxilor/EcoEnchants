package com.willfp.ecoenchants.proxy.v1_17_R1;

import com.willfp.ecoenchants.proxy.proxies.FastGetEnchantsProxy;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.item.ItemEnchantedBook;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class FastGetEnchants implements FastGetEnchantsProxy {
    @Override
    public Map<Enchantment, Integer> getEnchantmentsOnItem(@NotNull final ItemStack itemStack,
                                                           final boolean checkStored) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagList enchantmentNBT = checkStored && itemStack.getType() == Material.ENCHANTED_BOOK ? ItemEnchantedBook.d(nmsStack) : nmsStack.getEnchantments();
        Map<Enchantment, Integer> foundEnchantments = new HashMap<>();

        for (NBTBase base : enchantmentNBT) {
            NBTTagCompound compound = (NBTTagCompound) base;
            String key = compound.getString("id");
            int level = '\uffff' & compound.getShort("lvl");

            Enchantment found = Enchantment.getByKey(CraftNamespacedKey.fromStringOrNull(key));
            if (found != null) {
                foundEnchantments.put(found, level);
            }
        }
        return foundEnchantments;
    }

    @Override
    public int getLevelOnItem(@NotNull final ItemStack itemStack,
                              @NotNull final Enchantment enchantment,
                              final boolean checkStored) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagList enchantmentNBT = checkStored && itemStack.getType() == Material.ENCHANTED_BOOK ? ItemEnchantedBook.d(nmsStack) : nmsStack.getEnchantments();

        for (NBTBase base : enchantmentNBT) {
            NBTTagCompound compound = (NBTTagCompound) base;
            String key = compound.getString("id");
            if (!key.equals(enchantment.getKey().toString())) {
                continue;
            }

            return '\uffff' & compound.getShort("lvl");
        }
        return 0;
    }
}

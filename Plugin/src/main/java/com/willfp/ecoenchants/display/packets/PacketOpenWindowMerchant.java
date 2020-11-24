package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class PacketOpenWindowMerchant extends AbstractPacketAdapter {
    public PacketOpenWindowMerchant() {
        super(PacketType.Play.Server.OPEN_WINDOW_MERCHANT);
    }

    @Override
    public void onSend(PacketContainer packet) {
        List<MerchantRecipe> recipes = packet.getMerchantRecipeLists().readSafely(0);

        recipes = recipes.stream().peek(merchantRecipe -> {
            try {
                if(!EnchantmentTarget.ALL.getMaterials().contains(merchantRecipe.getResult().getType()))
                    return;

                // Enables removing final modifier
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);

                // Bukkit MerchantRecipe result
                Field fResult = merchantRecipe.getClass().getSuperclass().getDeclaredField("result");
                fResult.setAccessible(true);
                ItemStack result = EnchantDisplay.displayEnchantments(merchantRecipe.getResult());
                result = EnchantDisplay.addV(result);
                fResult.set(merchantRecipe, result);

                // Get NMS MerchantRecipe from CraftMerchantRecipe
                Field fHandle = merchantRecipe.getClass().getDeclaredField("handle");
                fHandle.setAccessible(true);
                Object handle = fHandle.get(merchantRecipe); // NMS Recipe
                modifiersField.setInt(fHandle, fHandle.getModifiers() & ~Modifier.FINAL); // Remove final

                // NMS MerchantRecipe
                Field fSelling = fHandle.get(merchantRecipe).getClass().getDeclaredField("sellingItem");
                fSelling.setAccessible(true);
                Object selling = fSelling.get(handle); // NMS Selling ItemStack
                modifiersField.setInt(fSelling, fSelling.getModifiers() & ~Modifier.FINAL);

                // Reflectively access CraftItemStack.class for respective version
                Class<?> craftItemStack = Class.forName("org.bukkit.craftbukkit." + EcoEnchantsPlugin.NMS_VERSION + ".inventory.CraftItemStack");

                // Bukkit Result ItemStack from NMS Result ItemStack
                ItemStack nmsSelling = (ItemStack) craftItemStack.getMethod("asBukkitCopy", selling.getClass()).invoke(null, selling);
                nmsSelling = EnchantDisplay.displayEnchantments(nmsSelling);
                nmsSelling = EnchantDisplay.addV(nmsSelling);
                fSelling.set(handle, craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, nmsSelling));

            } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }).collect(Collectors.toList());

        packet.getMerchantRecipeLists().writeSafely(0, recipes);
    }
}

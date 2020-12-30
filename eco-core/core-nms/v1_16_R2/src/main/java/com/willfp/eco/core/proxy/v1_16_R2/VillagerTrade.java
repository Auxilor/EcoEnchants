package com.willfp.eco.core.proxy.v1_16_R2;

import com.willfp.eco.core.proxy.proxies.VillagerTradeProxy;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftMerchantRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class VillagerTrade implements VillagerTradeProxy {
    @Override
    public void displayTradeEnchantments(@NotNull final MerchantRecipe merchantRecipe) {
        try {
            // Enables removing final modifier
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);

            // Bukkit MerchantRecipe result
            Field fResult = MerchantRecipe.class.getDeclaredField("result");
            fResult.setAccessible(true);
            ItemStack result = EnchantDisplay.displayEnchantments(merchantRecipe.getResult());
            EnchantDisplay.addV(result);
            fResult.set(merchantRecipe, result);

            // Get NMS MerchantRecipe from CraftMerchantRecipe
            Field fHandle = CraftMerchantRecipe.class.getDeclaredField("handle");
            fHandle.setAccessible(true);
            net.minecraft.server.v1_16_R2.MerchantRecipe handle = (net.minecraft.server.v1_16_R2.MerchantRecipe) fHandle.get(merchantRecipe); // NMS Recipe
            modifiersField.setInt(fHandle, fHandle.getModifiers() & ~Modifier.FINAL); // Remove final

            Field fSelling = net.minecraft.server.v1_16_R2.MerchantRecipe.class.getDeclaredField("sellingItem");
            fSelling.setAccessible(true);
            modifiersField.setInt(fSelling, fSelling.getModifiers() & ~Modifier.FINAL);

            ItemStack selling = CraftItemStack.asBukkitCopy(handle.sellingItem);
            EnchantDisplay.displayEnchantments(selling);
            EnchantDisplay.addV(selling);

            fSelling.set(handle, CraftItemStack.asNMSCopy(selling));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

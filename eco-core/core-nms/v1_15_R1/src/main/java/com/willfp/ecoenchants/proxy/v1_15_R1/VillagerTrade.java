package com.willfp.ecoenchants.proxy.v1_15_R1;

import com.willfp.ecoenchants.proxy.proxies.VillagerTradeProxy;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftMerchantRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public final class VillagerTrade implements VillagerTradeProxy {
    @Override
    public void displayTradeEnchantments(@NotNull final MerchantRecipe merchantRecipe) {
        try {
            // Bukkit MerchantRecipe result
            Field fResult = MerchantRecipe.class.getDeclaredField("result");
            fResult.setAccessible(true);
            ItemStack result = EnchantDisplay.displayEnchantments(merchantRecipe.getResult());
            EnchantDisplay.addV(result);
            fResult.set(merchantRecipe, result);

            // Get NMS MerchantRecipe from CraftMerchantRecipe
            Field fHandle = CraftMerchantRecipe.class.getDeclaredField("handle");
            fHandle.setAccessible(true);
            net.minecraft.server.v1_15_R1.MerchantRecipe handle = (net.minecraft.server.v1_15_R1.MerchantRecipe) fHandle.get(merchantRecipe); // NMS Recipe

            Field fSelling = net.minecraft.server.v1_15_R1.MerchantRecipe.class.getDeclaredField("sellingItem");
            fSelling.setAccessible(true);

            ItemStack selling = CraftItemStack.asBukkitCopy(handle.sellingItem);
            EnchantDisplay.displayEnchantments(selling);
            EnchantDisplay.addV(selling);

            fSelling.set(handle, CraftItemStack.asNMSCopy(selling));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

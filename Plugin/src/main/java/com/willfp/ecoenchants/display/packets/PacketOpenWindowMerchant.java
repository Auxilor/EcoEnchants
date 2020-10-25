package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public final class PacketOpenWindowMerchant extends AbstractPacketAdapter {
    public PacketOpenWindowMerchant() {
        super(PacketType.Play.Server.OPEN_WINDOW_MERCHANT);
    }

    @Override
    public void onSend(PacketContainer packet) {
        List<MerchantRecipe> recipes = packet.getMerchantRecipeLists().readSafely(0);

        recipes = recipes.stream().peek(merchantRecipe -> {
            try {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);

                Field fResult = merchantRecipe.getClass().getSuperclass().getDeclaredField("result");
                fResult.setAccessible(true);
                fResult.set(merchantRecipe, EnchantDisplay.displayEnchantments(merchantRecipe.getResult()));

                Field fHandle = merchantRecipe.getClass().getDeclaredField("handle");
                fHandle.setAccessible(true);
                Object handle = fHandle.get(merchantRecipe);

                modifiersField.setInt(fHandle, fHandle.getModifiers() & ~Modifier.FINAL);

                Field fSelling = fHandle.get(merchantRecipe).getClass().getDeclaredField("sellingItem");
                fSelling.setAccessible(true);

                Object selling = fSelling.get(handle);

                modifiersField.setInt(fSelling, fSelling.getModifiers() & ~Modifier.FINAL);

                ItemStack itemStack = (ItemStack) Class.forName("org.bukkit.craftbukkit." + EcoEnchantsPlugin.nmsVersion + ".inventory.CraftItemStack").getMethod("asBukkitCopy", selling.getClass()).invoke(null, selling);

                itemStack = EnchantDisplay.displayEnchantments(itemStack);

                fSelling.set(handle, Class.forName("org.bukkit.craftbukkit." + EcoEnchantsPlugin.nmsVersion + ".inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack));
            } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }).collect(Collectors.toList());

        packet.getMerchantRecipeLists().writeSafely(0, recipes);
    }
}

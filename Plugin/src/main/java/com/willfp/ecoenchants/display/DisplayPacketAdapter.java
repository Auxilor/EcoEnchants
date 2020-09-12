package com.willfp.ecoenchants.display;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.MerchantRecipe;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayPacketAdapter extends PacketAdapter {
    private static final List<PacketType> packets = Arrays.asList(
            PacketType.Play.Server.WINDOW_ITEMS,
            PacketType.Play.Server.SET_SLOT,
            PacketType.Play.Client.SET_CREATIVE_SLOT,
            PacketType.Play.Server.OPEN_WINDOW_MERCHANT
    );

    public DisplayPacketAdapter() {
        super(EcoEnchantsPlugin.getInstance(), packets);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(event.getPacket() == null) return;

        PacketType packetType = event.getPacketType();
        if (packetType.equals(PacketType.Play.Server.WINDOW_ITEMS)) {
            event.getPacket().getItemListModifier().modify(0, (itemStacks) -> {
                itemStacks.forEach(EnchantDisplay::displayEnchantments);
                return itemStacks;
            });
        } else if (packetType.equals(PacketType.Play.Server.SET_SLOT)) {
            event.getPacket().getItemModifier().modify(0, (item) -> {
                item = EnchantDisplay.displayEnchantments(item);
                return item;
            });
        } else if (packetType.equals(PacketType.Play.Server.OPEN_WINDOW_MERCHANT)) {
            List<MerchantRecipe> merchantRecipes = event.getPacket().getMerchantRecipeLists().readSafely(0);
            if (merchantRecipes != null) {
                List<MerchantRecipe> newList =
                        merchantRecipes.stream().map(oldRecipe -> {
                            MerchantRecipe recipe =
                                    new MerchantRecipe(EnchantDisplay.displayEnchantments(oldRecipe.getResult()),
                                    oldRecipe.getUses(),
                                    oldRecipe.getMaxUses(),
                                    oldRecipe.hasExperienceReward(),
                                    oldRecipe.getVillagerExperience(),
                                    oldRecipe.getPriceMultiplier());
                            recipe.setIngredients(oldRecipe.getIngredients());
                            return recipe;
                        }).collect(Collectors.toList());

                event.getPacket().getMerchantRecipeLists().writeSafely(0, newList);
            }
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if(event.getPacket() == null) return;

        if(!event.getPacketType().equals(PacketType.Play.Client.SET_CREATIVE_SLOT)) return;

        event.getPacket().getItemModifier().modify(0, (item) -> {
            item = EnchantDisplay.revertDisplay(item);
            return item;
        });
    }
}

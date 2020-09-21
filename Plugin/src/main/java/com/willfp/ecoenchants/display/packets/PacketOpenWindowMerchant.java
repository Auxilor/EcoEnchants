package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;
import java.util.stream.Collectors;

public final class PacketOpenWindowMerchant extends AbstractPacketAdapter {
    private PacketOpenWindowMerchant() {
        super(PacketType.Play.Server.OPEN_WINDOW_MERCHANT);
    }

    @Override
    public void onSend(PacketContainer packet) {
        List<MerchantRecipe> merchantRecipes = packet.getMerchantRecipeLists().readSafely(0);
        if(merchantRecipes == null)
            return;

        List<MerchantRecipe> newList =
                merchantRecipes.stream().map(oldRecipe -> {
                    MerchantRecipe recipe =
                            new MerchantRecipe(EnchantDisplay.displayEnchantments(oldRecipe.getResult()),
                                    oldRecipe.getUses(),
                                    oldRecipe.getMaxUses(),
                                    oldRecipe.hasExperienceReward(),
                                    oldRecipe.getVillagerExperience(),
                                    oldRecipe.getPriceMultiplier());
                    List<ItemStack> ingredients = oldRecipe.getIngredients();
                    ingredients.forEach(EnchantDisplay::displayEnchantments);
                    recipe.setIngredients(ingredients);
                    return recipe;
                }).collect(Collectors.toList());

        packet.getMerchantRecipeLists().writeSafely(0, newList);
    }
}

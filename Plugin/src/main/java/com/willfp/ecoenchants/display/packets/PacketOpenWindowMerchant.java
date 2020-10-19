package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.inventory.MerchantRecipe;

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

        recipes = recipes.stream().map(merchantRecipe -> {
            MerchantRecipe newRecipe = new MerchantRecipe(
                    EnchantDisplay.displayEnchantments(merchantRecipe.getResult()),
                    merchantRecipe.getUses(),
                    merchantRecipe.getMaxUses(),
                    merchantRecipe.hasExperienceReward(),
                    merchantRecipe.getVillagerExperience(),
                    merchantRecipe.getPriceMultiplier()
            );
            newRecipe.setIngredients(merchantRecipe.getIngredients().stream().map(EnchantDisplay::displayEnchantments).collect(Collectors.toList()));
            return newRecipe;
        }).collect(Collectors.toList());

        packet.getMerchantRecipeLists().writeSafely(0, recipes);
    }
}

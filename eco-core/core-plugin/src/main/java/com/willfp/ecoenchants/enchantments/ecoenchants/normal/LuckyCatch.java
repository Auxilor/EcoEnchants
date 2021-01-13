package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LuckyCatch extends EcoEnchant {
    public LuckyCatch() {
        super(
                "lucky_catch", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onFish(@NotNull final PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        int level = EnchantChecks.getItemLevel(item, this);

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        Entity caught = event.getCaught();

        if (!(caught instanceof Item)) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        Item caughtItem = (Item) caught;

        caughtItem.getItemStack().setAmount(caughtItem.getItemStack().getAmount() * 2);
    }
}

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

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @EventHandler
    public void onFish(@NotNull final PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            return;
        }

        int level = EnchantChecks.getItemLevel(item, this);

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        Entity caught = event.getCaught();

        if (!(caught instanceof Item caughtItem)) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        caughtItem.getItemStack().setAmount(caughtItem.getItemStack().getAmount() * 2);
    }
}

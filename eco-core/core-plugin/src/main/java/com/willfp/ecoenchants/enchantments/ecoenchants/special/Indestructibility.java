package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Indestructibility extends EcoEnchant {
    public Indestructibility() {
        super(
                "indestructibility", EnchantmentType.SPECIAL
        );
    }

    @EventHandler
    public void onItemDamage(@NotNull final PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(event.getPlayer().getWorld())) {
            return;
        }

        if (!this.areRequirementsMet(event.getPlayer())) {
            return;
        }

        double level = EnchantChecks.getItemLevel(item, this);
        double levelBonus = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "level-bonus");

        if (NumberUtils.randFloat(0, 1) < (100 / (level + (1 + levelBonus)) / 100)) {
            return;
        }

        event.setCancelled(true);
        event.setDamage(0);
    }
}

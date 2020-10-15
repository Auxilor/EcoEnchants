package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
public final class Indestructibility extends EcoEnchant {
    public Indestructibility() {
        super(
                new EcoEnchantBuilder("indestructibility", EnchantmentType.SPECIAL)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        if (!EnchantChecks.item(item, this)) return;

        double level = EnchantChecks.getItemLevel(item, this);
        double levelbonus = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "level-bonus");

        if(NumberUtils.randFloat(0, 1) < (100/ (level + (1 + levelbonus))/100)) return;

        event.setCancelled(true);
        event.setDamage(0);
    }
}

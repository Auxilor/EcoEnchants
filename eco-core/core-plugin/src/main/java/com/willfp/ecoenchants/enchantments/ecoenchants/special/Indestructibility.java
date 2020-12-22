package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
public class Indestructibility extends EcoEnchant {
    public Indestructibility() {
        super(
                "indestructibility", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        if (!EnchantChecks.item(item, this)) return;

        if(this.getDisabledWorlds().contains(event.getPlayer().getWorld())) return;

        double level = EnchantChecks.getItemLevel(item, this);
        double levelbonus = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "level-bonus");

        if(NumberUtils.randFloat(0, 1) < (100/ (level + (1 + levelbonus))/100)) return;

        event.setCancelled(true);
        event.setDamage(0);
    }
}

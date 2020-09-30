package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
public class Stamina extends EcoEnchant {
    public Stamina() {
        super(
                new EcoEnchantBuilder("stamina", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onStaminaHunger(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(!player.isSprinting()) return;

        if(!EnchantChecks.boots(player, this)) return;
        if(event.getFoodLevel() > player.getFoodLevel()) return;

        int level = EnchantChecks.getBootsLevel(player, this);
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        event.setCancelled(true);
    }
}

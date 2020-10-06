package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
public final class HungerCurse extends EcoEnchant {
    public HungerCurse() {
        super(
                new EcoEnchantBuilder("hunger_curse", EnchantmentType.CURSE, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(!EnchantChecks.helmet(player, this)) return;
        if(event.getFoodLevel() > player.getFoodLevel()) return;

        int delta = player.getFoodLevel() - event.getFoodLevel();
        delta *= this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "times-more-hunger");

        event.setFoodLevel(player.getFoodLevel() - delta);
    }
}

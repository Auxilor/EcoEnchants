package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerCurse extends EcoEnchant {
    public HungerCurse() {
        super(
                new EcoEnchantBuilder("hunger_curse", EnchantmentType.CURSE, Target.Applicable.HELMET, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(!HasEnchant.playerHelmet(player, this)) return;
        if(event.getFoodLevel() > player.getFoodLevel()) return;

        int delta = player.getFoodLevel() - event.getFoodLevel();
        delta *= this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "times-more-hunger");

        event.setFoodLevel(player.getFoodLevel() - delta);
    }
}

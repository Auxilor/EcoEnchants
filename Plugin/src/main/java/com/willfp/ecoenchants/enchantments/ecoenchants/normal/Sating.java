package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Sating extends EcoEnchant {
    public Sating() {
        super(
                new EcoEnchantBuilder("sating", EnchantmentType.NORMAL, Target.Applicable.HELMET, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onSatingHunger(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(!HasEnchant.playerHelmet(player, this)) return;
        if(event.getFoodLevel() > player.getFoodLevel()) return;

        int level = HasEnchant.getPlayerHelmetLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");

        if(Rand.randFloat(0, 1) > level * 0.01 * chance)
            return;

        event.setCancelled(true);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@SuppressWarnings("deprecation")
public class Extinguishing extends EcoEnchant {
    public Extinguishing() {
        super(
                new EcoEnchantBuilder("extinguishing", EnchantmentType.NORMAL, Target.Applicable.ARMOR, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onExtinguishingHurt(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK))
            return;

        Player player = (Player) event.getEntity();

        int totalExtinguishingPoints = HasEnchant.getArmorPoints(player, this, false);

        if (totalExtinguishingPoints == 0)
            return;

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-point");
        if (Rand.randFloat(0, 1) > totalExtinguishingPoints * 0.01 * chance)
            return;

        player.setFireTicks(0);
    }

}

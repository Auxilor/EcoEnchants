package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class Preservation extends EcoEnchant {
    public Preservation() {
        super(
                new EcoEnchantBuilder("preservation", EnchantmentType.SPECIAL, Target.Applicable.ARMOR, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onPreservationHurt(EntityDamageEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;

        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        int totalPreservationPoints = HasEnchant.getArmorPoints(player, this, true);

        if(totalPreservationPoints == 0)
            return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-less-per-level");

        double multiplier = 1 - (reduction / 100 * totalPreservationPoints);

        event.setDamage(event.getDamage() * multiplier);
    }
}

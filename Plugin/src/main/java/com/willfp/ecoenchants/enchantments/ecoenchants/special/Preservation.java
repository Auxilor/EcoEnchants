package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
public class Preservation extends EcoEnchant {
    public Preservation() {
        super(
                new EcoEnchantBuilder("preservation", EnchantmentType.SPECIAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onPreservationHurt(EntityDamageEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        int totalPreservationPoints = EnchantChecks.getArmorPoints(player, this, 1);

        if (totalPreservationPoints == 0)
            return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-less-per-level");

        double multiplier = 1 - (reduction/100 * totalPreservationPoints);

        event.setDamage(event.getDamage() * multiplier);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Chopless extends EcoEnchant {
    public Chopless() {
        super(
                new EcoEnchantBuilder("chopless", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onPreservationHurt(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player))
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if(!damager.getInventory().getItemInMainHand().getType().toString().endsWith("_AXE"))
            return;

        int totalChoplessPoints = EnchantChecks.getArmorPoints(player, this, 1);

        if (totalChoplessPoints == 0)
            return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-less-per-level");

        double multiplier = 1 - (reduction/100 * totalChoplessPoints);

        event.setDamage(event.getDamage() * multiplier);
    }
}

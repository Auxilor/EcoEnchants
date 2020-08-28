package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@SuppressWarnings("deprecation")
public class Chopless extends EcoEnchant {
    public Chopless() {
        super(
                new EcoEnchantBuilder("chopless", EnchantmentType.NORMAL, Target.Applicable.ARMOR, 4.0)
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

        if(!Target.Applicable.AXE.getMaterials().contains(damager.getInventory().getItemInMainHand().getType()))
            return;

        int totalChoplessPoints = HasEnchant.getArmorPoints(player, this, true);

        if (totalChoplessPoints == 0)
            return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-less-per-level");

        double multiplier = 1 - (reduction/100 * totalChoplessPoints);

        event.setDamage(event.getDamage() * multiplier);
    }
}

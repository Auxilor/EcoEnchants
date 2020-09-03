package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Defender extends EcoEnchant {
    public Defender() {
        super(
                new EcoEnchantBuilder("defender", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.BOW, Target.Applicable.CROSSBOW}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;
        if(!(((Arrow) event.getDamager()).getShooter() instanceof Player))
            return;
        if (!(event.getEntity() instanceof Tameable))
            return;

        Player player = (Player) ((Arrow) event.getDamager()).getShooter();
        Tameable entity = (Tameable) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();
        if(entity.getOwner() == null) return;
        if(!entity.getOwner().equals(player)) return;

        if (!EnchantChecks.arrow(arrow, this)) return;

        event.setCancelled(true);
    }
}

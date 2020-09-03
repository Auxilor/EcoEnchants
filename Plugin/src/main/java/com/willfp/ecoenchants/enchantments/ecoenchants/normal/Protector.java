package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Protector extends EcoEnchant {
    public Protector() {
        super(
                new EcoEnchantBuilder("protector", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.SWORD, Target.Applicable.AXE}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void protectorHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof Tameable))
            return;

        Player player = (Player) event.getDamager();
        Tameable entity = (Tameable) event.getEntity();
        if(entity.getOwner() == null) return;
        if(!entity.getOwner().equals(player)) return;

        if (!EnchantChecks.mainhand(player, this)) return;

        event.setCancelled(true);
    }
}

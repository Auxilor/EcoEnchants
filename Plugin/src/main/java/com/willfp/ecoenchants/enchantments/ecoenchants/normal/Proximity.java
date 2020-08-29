package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Proximity extends EcoEnchant {
    public Proximity() {
        super(
                new EcoEnchantBuilder("proximity", EnchantmentType.NORMAL, Target.Applicable.SWORD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void proximityHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player))
            return;
        if(!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) event.getDamager();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(!HasEnchant.playerHeld(player, this)) return;

        double distance = player.getLocation().distance(victim.getLocation());

        double decreaseAfter = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "when-closer-than-blocks");

        int level = HasEnchant.getPlayerLevel(player, this);

        if(distance <= decreaseAfter) {
            double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");
            double finalMultiplier = (multiplier / 100 * level) + 1;
            event.setDamage(event.getDamage() * finalMultiplier);
        }
    }
}

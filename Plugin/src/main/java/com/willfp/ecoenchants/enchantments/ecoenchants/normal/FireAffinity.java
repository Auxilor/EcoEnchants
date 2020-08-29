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

public class FireAffinity extends EcoEnchant {
    public FireAffinity() {
        super(
                new EcoEnchantBuilder("fire_affinity", EnchantmentType.NORMAL, Target.Applicable.SWORD, 4.01)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void fireAffinityHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player))
            return;
        if(!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) event.getDamager();

        if(!HasEnchant.playerHeld(player, this)) return;

        if(player.getFireTicks() == 0) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");

        double finalMultiplier = (multiplier / 100 * level) + 1;

        event.setDamage(event.getDamage() * finalMultiplier);
    }
}

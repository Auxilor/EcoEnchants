package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class Pentashot extends EcoEnchant {
    public Pentashot() {
        super(
                new EcoEnchantBuilder("pentashot", EnchantmentType.SPECIAL, Target.Applicable.BOW, 4.01)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onPentashotShoot(EntityShootBowEvent event) {
        if(event.getProjectile().getType() != EntityType.ARROW)
            return;

        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(!HasEnchant.playerHeld(player, this)) return;

        for(int i = -2; i <= 2; i += 1) {
            if(i == 0) continue;

            Vector velocity = event.getProjectile().getVelocity();

            float radians = (float) ((float) i * Math.toRadians(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "angle")));
            velocity.rotateAroundY(radians);

            Arrow arrow = player.launchProjectile(Arrow.class, velocity);
            if(HasEnchant.playerHeld(player, Enchantment.ARROW_FIRE)) arrow.setFireTicks(Integer.MAX_VALUE);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Marksman extends EcoEnchant {
    public Marksman() {
        super(
                new EcoEnchantBuilder("marksman", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.CROSSBOW, Target.Applicable.BOW}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onMarksmanShoot(ProjectileLaunchEvent event) {
        if(event.getEntityType() != EntityType.ARROW)
            return;

        if(!(event.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) event.getEntity().getShooter();

        if(!HasEnchant.playerHeld(player, this)) return;

        if(!(event.getEntity() instanceof Arrow)) return;
        Arrow a = (Arrow) event.getEntity();
        a.setGravity(false);

        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "remove-arrow-after-ticks");

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!a.isOnGround()) {
                    a.remove();
                }
            }
        }.runTaskLater(EcoEnchantsPlugin.getInstance(), ticks);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
public class Collateral extends EcoEnchant {
    public Collateral() {
        super(
                new EcoEnchantBuilder("collateral", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onCollateralShoot(ProjectileLaunchEvent event) {
        if (event.getEntityType() != EntityType.ARROW)
            return;

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) event.getEntity().getShooter();

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow a = (Arrow) event.getEntity();

        a.setPierceLevel(level);
    }
}

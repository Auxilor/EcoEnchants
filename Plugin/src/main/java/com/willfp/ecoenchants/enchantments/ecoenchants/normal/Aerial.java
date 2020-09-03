package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Aerial extends EcoEnchant {
    public Aerial() {
        super(
                new EcoEnchantBuilder("aerial", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.BOW, Target.Applicable.CROSSBOW}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity() instanceof Arrow))
            return;

        if(!(event.getEntity().getShooter() instanceof Player))
            return;

        Arrow arrow = (Arrow) event.getEntity();
        Player player = (Player) arrow.getShooter();

        if(player.isOnGround()) return;

        arrow.setMetadata("shot-in-air", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;

        Arrow arrow = (Arrow) event.getDamager();
        if(!EnchantChecks.arrow(arrow, this)) return;
        if(!arrow.hasMetadata("shot-in-air")) return;

        int level = EnchantChecks.getArrowLevel(arrow, this);

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        double reduction = 1 + (multiplier * level);
        event.setDamage(damage * reduction);
    }
}

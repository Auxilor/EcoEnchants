package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Optics extends EcoEnchant {
    public Optics() {
        super(
                new EcoEnchantBuilder("optics", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.BOW, Target.Applicable.CROSSBOW}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onOpticsDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;

        if (!(((Arrow) event.getDamager()).getShooter() instanceof Player)) return;

        Player player = (Player) ((Arrow) event.getDamager()).getShooter();
        Arrow arrow = (Arrow) event.getDamager();

        if (!EnchantChecks.arrow(arrow, this)) return;

        Location land = arrow.getLocation();
        Location source = player.getLocation();

        double distance = land.distance(source);

        int level = EnchantChecks.getArrowLevel(arrow, this);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "block-multiplier");

        double damageMultiplier = (distance * level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}
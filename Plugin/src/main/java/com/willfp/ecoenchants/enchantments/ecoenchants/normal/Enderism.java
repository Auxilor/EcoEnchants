package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Enderism extends EcoEnchant {
    public Enderism() {
        super(
                new EcoEnchantBuilder("enderism", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;
        if (!(((Arrow) event.getDamager()).getShooter() instanceof Player))
            return;

        Player player = (Player) ((Arrow) event.getDamager()).getShooter();
        Arrow arrow = (Arrow) event.getDamager();

        assert player != null;
        if(!player.getWorld().getEnvironment().equals(World.Environment.THE_END))
            return;

        if (!EnchantChecks.arrow(arrow, this)) return;

        int level = EnchantChecks.getArrowLevel(arrow, this);
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "per-level-multiplier");

        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }
}

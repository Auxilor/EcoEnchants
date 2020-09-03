package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
public class Extinguishing extends EcoEnchant {
    public Extinguishing() {
        super(
                new EcoEnchantBuilder("extinguishing", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onExtinguishingHurt(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK))
            return;

        Player player = (Player) event.getEntity();

        int totalExtinguishingPoints = EnchantChecks.getArmorPoints(player, this, 0);

        if (totalExtinguishingPoints == 0)
            return;

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-point");
        if (NumberUtils.randFloat(0, 1) > totalExtinguishingPoints * 0.01 * chance)
            return;

        player.setFireTicks(0);
    }

}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
public class Arcanic extends EcoEnchant {
    public Arcanic() {
        super(
                new EcoEnchantBuilder("arcanic", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onEffect(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getCause().equals(EntityDamageEvent.DamageCause.POISON) || event.getCause().equals(EntityDamageEvent.DamageCause.WITHER)))
            return;

        Player player = (Player) event.getEntity();

        int totalArcanicPoints = EnchantChecks.getArmorPoints(player, this, 0);

        if (totalArcanicPoints == 0)
            return;

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-point");
        if (NumberUtils.randFloat(0, 1) > totalArcanicPoints * 0.01 * chance)
            return;

        event.setCancelled(true);
    }
}

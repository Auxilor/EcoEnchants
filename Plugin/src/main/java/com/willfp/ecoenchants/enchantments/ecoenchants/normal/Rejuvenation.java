package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
public final class Rejuvenation extends EcoEnchant {
    public Rejuvenation() {
        super(
                "rejuvenation", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onRejuvenationHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED) && !event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.REGEN))
            return;

        Player player = (Player) event.getEntity();

        int totalRejuvenationPoints = EnchantChecks.getArmorPoints(player, this, 0);

        if (totalRejuvenationPoints == 0)
            return;

        double amount = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "per-point-multiplier");
        amount = amount * totalRejuvenationPoints;
        amount += 1;

        event.setAmount(event.getAmount() * amount);
    }
}

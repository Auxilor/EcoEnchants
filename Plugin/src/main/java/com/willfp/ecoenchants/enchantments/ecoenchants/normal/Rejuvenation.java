package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
public class Rejuvenation extends EcoEnchant {
    public Rejuvenation() {
        super(
                new EcoEnchantBuilder("rejuvenation", EnchantmentType.NORMAL, Target.Applicable.ARMOR, 4.0)
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

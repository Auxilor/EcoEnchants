package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.jetbrains.annotations.NotNull;

public class Rejuvenation extends EcoEnchant {
    public Rejuvenation() {
        super(
                "rejuvenation", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onRejuvenationHeal(@NotNull final EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED) && !event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.REGEN)) {
            return;
        }

        int totalRejuvenationPoints = EnchantChecks.getArmorPoints(player, this, 0);
        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (totalRejuvenationPoints == 0) {
            return;
        }

        double amount = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "per-point-multiplier");
        amount = amount * totalRejuvenationPoints;
        amount += 1;

        event.setAmount(event.getAmount() * amount);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ShotAssist extends EcoEnchant {
    public ShotAssist() {
        super(
                "shot_assist", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onHit(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) {
            return;
        }

        if (!(((Arrow) event.getDamager()).getShooter() instanceof Player player)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        assert player != null;

        int points = EnchantChecks.getArmorPoints(player, this, 0);

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (points == 0) {
            return;
        }

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double reduction = 1 + (multiplier * points);
        event.setDamage(damage * reduction);
    }
}

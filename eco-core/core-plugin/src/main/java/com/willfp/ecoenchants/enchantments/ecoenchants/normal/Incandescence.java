package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class Incandescence extends EcoEnchant {
    public Incandescence() {
        super(
                "incandescence", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onIncandescenceHurt(@NotNull final EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.THORNS) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!(event.getDamager() instanceof LivingEntity victim)) {
            return;
        }

        int totalIncandescencePoints = EnchantChecks.getArmorPoints(player, this);

        if (totalIncandescencePoints == 0) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!AntigriefManager.canInjure(player, victim)) {
            return;
        }

        this.getPlugin().getScheduler().runLater(() -> victim.setFireTicks(totalIncandescencePoints
                        * this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-point")
                        + this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-ticks")),
                1);
    }
}

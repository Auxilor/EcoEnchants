package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Rebounding extends EcoEnchant {
    public Rebounding() {
        super(
                "rebounding", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (!(event.getDamager() instanceof LivingEntity attacker)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!this.areRequirementsMet(victim)) {
            return;
        }

        int level = EnchantChecks.getArmorPoints(victim, this);

        if (level == 0) {
            return;
        }

        if (this.getDisabledWorlds().contains(attacker.getWorld())) {
            return;
        }

        Vector vector = attacker.getLocation().toVector().clone().subtract(victim.getLocation().toVector()).normalize()
                .multiply((level * (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-multiplier") - 1)) + 1);

        if (!VectorUtils.isFinite(vector)) {
            return;
        }

        vector.setY(0.2);

        if (!VectorUtils.isFinite(vector)) {
            return;
        }

        attacker.setVelocity(vector);
    }
}

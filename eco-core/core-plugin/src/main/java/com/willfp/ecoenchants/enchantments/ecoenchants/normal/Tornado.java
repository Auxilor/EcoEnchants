package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Tornado extends EcoEnchant {
    public Tornado() {
        super(
                "tornado", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, int level, @NotNull EntityDamageByEntityEvent event) {
        double baseVelocity = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-per-level");
        double yVelocity = baseVelocity * level;

        Vector toAdd = new Vector(0, yVelocity, 0);

        this.getPlugin().getScheduler().runLater(() -> {
            victim.setVelocity(victim.getVelocity().clone().add(toAdd));
        }, 1);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Criticals extends EcoEnchant {
    public Criticals() {
        super(
                "criticals", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return NumberUtils.format(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier") * 100 * level);
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(attacker.getFallDistance() > 0 && !attacker.isOnGround())) {
            return;
        }

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "use-additive")) {
            damage = damage / 1.5;
            double bonus = damage * (multiplier * level);
            damage = damage + bonus + damage / 2;
            event.setDamage(damage);
        } else {
            double bonus = 1 + (multiplier * level);
            event.setDamage(damage * bonus);
        }
    }
}

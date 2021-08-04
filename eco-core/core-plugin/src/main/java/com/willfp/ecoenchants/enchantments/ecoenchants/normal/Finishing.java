package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Finishing extends EcoEnchant {
    public Finishing() {
        super(
                "finishing", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        double minhealth = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "minimum-health-per-level");
        if (victim.getHealth() > level * minhealth) {
            return;
        }

        event.setDamage(30); // cba to do this properly
    }
}

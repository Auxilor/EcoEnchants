package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Sickening extends EcoEnchant {
    public Sickening() {
        super(
                "sickening", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        int duration = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "duration-per-level");

        victim.setVelocity(new Vector(0, 0, 0));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration * level, level));
    }
}

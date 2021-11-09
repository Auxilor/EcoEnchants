package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Bladed extends EcoEnchant {
    public Bladed() {
        super(
                "bladed", EnchantmentType.SPECIAL
        );
    }

    @Override
    public void onTridentDamage(@NotNull final LivingEntity attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                final int level,
                                @NotNull final EntityDamageByEntityEvent event) {
        if (victim instanceof Player && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-players")) {
            return;
        }
        double baseDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-multiplier");
        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = 1 + (multiplier * level) + baseDamage;
        event.setDamage(damage * bonus);
    }
}

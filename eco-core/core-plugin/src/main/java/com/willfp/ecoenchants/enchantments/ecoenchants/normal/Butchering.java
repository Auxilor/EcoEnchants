package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Butchering extends EcoEnchant {
    public Butchering() {
        super(
                "butchering", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (victim instanceof Monster) {
            return;
        }

        if (victim instanceof Player) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");
        event.setDamage(event.getDamage() + (level * multiplier));
    }
}

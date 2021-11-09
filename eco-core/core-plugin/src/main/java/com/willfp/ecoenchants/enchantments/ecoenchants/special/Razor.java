package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Razor extends EcoEnchant {
    public Razor() {
        super(
                "razor", EnchantmentType.SPECIAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (victim instanceof Player && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-players")) {
            return;
        }
        double perLevelMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double baseDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-damage");
        double extra = (level * perLevelMultiplier) + baseDamage;
        if (this.getConfig().getBool((EcoEnchants.CONFIG_LOCATION) + "decrease-if-cooldown") && attacker instanceof Player) {
            extra *= ((Player) attacker).getAttackCooldown();
        }

        event.setDamage(event.getDamage() + extra);
    }
}

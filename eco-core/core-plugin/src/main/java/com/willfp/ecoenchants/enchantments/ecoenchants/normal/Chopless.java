package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class Chopless extends EcoEnchant {
    public Chopless() {
        super(
                "chopless", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onDamageWearingArmor(@NotNull final LivingEntity victim,
                                     final int level,
                                     @NotNull final EntityDamageEvent event) {
        if (victim.getEquipment() == null) {
            return;
        }

        if (!victim.getEquipment().getItemInMainHand().getType().toString().endsWith("_AXE")) {
            return;
        }

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-less-per-level");

        double multiplier = 1 - ((reduction / 100) * level);

        event.setDamage(event.getDamage() * multiplier);
    }
}

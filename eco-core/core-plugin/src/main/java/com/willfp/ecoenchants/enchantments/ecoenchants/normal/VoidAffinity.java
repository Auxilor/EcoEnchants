package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class VoidAffinity extends EcoEnchant {
    public VoidAffinity() {
        super(
                "void_affinity", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Trident trident, int level, @NotNull EntityDamageByEntityEvent event) {
        if(!attacker.getWorld().getEnvironment().equals(World.Environment.THE_END))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }
}

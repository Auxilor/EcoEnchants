package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class NetherInfusion extends EcoEnchant {
    public NetherInfusion() {
        super(
                "nether_infusion", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, int level, @NotNull EntityDamageByEntityEvent event) {
        if(!attacker.getWorld().getEnvironment().equals(World.Environment.NETHER))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }
}

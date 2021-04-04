package com.willfp.ecoenchants.biomes;

import com.willfp.eco.core.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BiomesEnchantment extends EcoEnchant {
    private static final String MULTIPLIER_KEY = "multiplier";

    protected BiomesEnchantment(@NotNull final String key,
                                @NotNull final EnchantmentType type,
                                @NotNull final Prerequisite... prerequisites) {
        super(key, type, prerequisites);
    }

    public abstract boolean isValid(@NotNull Biome biome);

    private boolean isInBiome(@NotNull final LivingEntity entity) {
        Biome entityBiome = entity.getLocation().getBlock().getBiome();
        return isValid(entityBiome);
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!isInBiome(attacker)) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + MULTIPLIER_KEY);
        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }

    @Override
    public void onDamageWearingArmor(@NotNull final LivingEntity victim,
                                     final int level,
                                     @NotNull final EntityDamageEvent event) {
        if (!isInBiome(victim)) {
            return;
        }

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "reduction-per-level");
        double multiplier = 1 - ((reduction / 100) * level);
        event.setDamage(event.getDamage() * multiplier);
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!isInBiome(attacker)) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + MULTIPLIER_KEY);
        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }

    @Override
    public void onTridentDamage(@NotNull final LivingEntity attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                final int level,
                                @NotNull final EntityDamageByEntityEvent event) {
        if (!isInBiome(attacker)) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + MULTIPLIER_KEY);
        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }
}

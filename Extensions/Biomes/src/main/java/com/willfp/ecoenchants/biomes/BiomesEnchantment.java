package com.willfp.ecoenchants.biomes;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.block.Biome;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class BiomesEnchantment extends EcoEnchant {
    protected BiomesEnchantment(String key, EnchantmentType type, Prerequisite... prerequisites) {
        super(key, type, prerequisites);
    }

    public abstract boolean isValid(Biome biome);

    private boolean isInBiome(LivingEntity entity) {
        Biome entityBiome = entity.getLocation().getBlock().getBiome();
        return isValid(entityBiome);
    }

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(!isInBiome(attacker))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }

    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if(!isInBiome(victim))
            return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "reduction-per-level");
        double multiplier = 1 - ((reduction/100) * level);
        event.setDamage(event.getDamage() * multiplier);
    }

    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        if(!isInBiome(attacker))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }

    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        if(!isInBiome(attacker))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }
}

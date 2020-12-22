package com.willfp.ecoenchants.summoning.enchants;

import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.summoning.SummoningEnchantment;
import com.willfp.ecoenchants.summoning.SummoningType;
import org.bukkit.entity.EntityType;

public class Crawler extends SummoningEnchantment {
    public Crawler() {
        super("crawler", EnchantmentType.NORMAL, SummoningType.TRIDENT);
    }

    @Override
    public EntityType getEntity() {
        return EntityType.CAVE_SPIDER;
    }
}

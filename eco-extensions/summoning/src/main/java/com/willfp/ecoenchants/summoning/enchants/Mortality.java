package com.willfp.ecoenchants.summoning.enchants;

import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.summoning.SummoningEnchantment;
import com.willfp.ecoenchants.summoning.SummoningType;
import org.bukkit.entity.EntityType;

public class Mortality extends SummoningEnchantment {
    public Mortality() {
        super("mortality", EnchantmentType.NORMAL, SummoningType.RANGED);
    }

    @Override
    public EntityType getEntity() {
        return EntityType.WITHER_SKELETON;
    }
}

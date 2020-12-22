package com.willfp.ecoenchants.summoning.enchants;

import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.summoning.SummoningEnchantment;
import com.willfp.ecoenchants.summoning.SummoningType;
import org.bukkit.entity.EntityType;

public class Metallic extends SummoningEnchantment {
    public Metallic() {
        super("metallic", EnchantmentType.SPECIAL, SummoningType.MELEE);
    }

    @Override
    public EntityType getEntity() {
        return EntityType.IRON_GOLEM;
    }
}

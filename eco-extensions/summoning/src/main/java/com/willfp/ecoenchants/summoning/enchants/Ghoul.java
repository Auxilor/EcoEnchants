package com.willfp.ecoenchants.summoning.enchants;

import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.summoning.SummoningEnchantment;
import com.willfp.ecoenchants.summoning.SummoningType;
import org.bukkit.entity.EntityType;

public class Ghoul extends SummoningEnchantment {
    public Ghoul() {
        super("ghoul", EnchantmentType.NORMAL, SummoningType.RANGED);
    }

    @Override
    public EntityType getEntity() {
        return EntityType.ZOMBIE;
    }
}

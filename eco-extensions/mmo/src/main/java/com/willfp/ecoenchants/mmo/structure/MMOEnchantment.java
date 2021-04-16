package com.willfp.ecoenchants.mmo.structure;

import com.willfp.eco.core.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.mmo.MMOPrerequisites;

public abstract class MMOEnchantment extends EcoEnchant implements MMOEnchant {
    protected MMOEnchantment(String key, EnchantmentType type, Prerequisite... prerequisites) {
        super(key, type, MMOPrerequisites.append(prerequisites, MMOPrerequisites.HAS_MMOCORE));

        MMOEnchant.REGISTRY.add(this);
    }
}

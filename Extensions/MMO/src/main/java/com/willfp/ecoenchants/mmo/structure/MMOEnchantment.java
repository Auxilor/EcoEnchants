package com.willfp.ecoenchants.mmo.structure;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.mmo.MMOMain;
import com.willfp.ecoenchants.mmo.MMOPrerequisites;
import com.willfp.ecoenchants.util.optional.Prerequisite;

public abstract class MMOEnchantment extends EcoEnchant implements MMOEnchant {
    protected MMOEnchantment(String key, EnchantmentType type, Prerequisite... prerequisites) {
        super(key, type, MMOMain.class, MMOPrerequisites.append(prerequisites, MMOPrerequisites.HAS_MMOCORE));

        MMOEnchant.REGISTRY.add(this);
    }
}

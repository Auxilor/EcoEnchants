package com.willfp.ecoenchants.mmo;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.util.optional.Prerequisite;

public abstract class MMOEnchantment extends EcoEnchant {
    protected MMOEnchantment(String key, EnchantmentType type, Prerequisite... prerequisites) {
        super(key, type, MMOMain.class, MMOPrerequisites.append(prerequisites, MMOPrerequisites.HAS_MMOCORE));
    }
}

package com.willfp.ecoenchants.mmo.structure;

import com.willfp.eco.core.Prerequisite;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import com.willfp.ecoenchants.mmo.MMOPrerequisites;

public abstract class MMOSpell extends Spell implements MMOEnchant {
    protected MMOSpell(String key, Prerequisite... prerequisites) {
        super(key, MMOPrerequisites.append(prerequisites, MMOPrerequisites.HAS_MMOCORE));

        MMOEnchant.REGISTRY.add(this);
    }
}

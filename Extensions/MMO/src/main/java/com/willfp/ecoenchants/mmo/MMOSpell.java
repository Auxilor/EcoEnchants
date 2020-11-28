package com.willfp.ecoenchants.mmo;

import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import com.willfp.ecoenchants.util.optional.Prerequisite;

public abstract class MMOSpell extends Spell {
    protected MMOSpell(String key, Prerequisite... prerequisites) {
        super(key, MMOMain.class, MMOPrerequisites.append(prerequisites, MMOPrerequisites.HAS_MMOCORE));
    }
}

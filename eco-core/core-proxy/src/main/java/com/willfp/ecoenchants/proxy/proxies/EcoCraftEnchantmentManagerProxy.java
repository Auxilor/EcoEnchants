package com.willfp.ecoenchants.proxy.proxies;

import com.willfp.eco.core.proxy.AbstractProxy;

public interface EcoCraftEnchantmentManagerProxy extends AbstractProxy {
    /**
     * Re-Register new CraftEnchantments for all vanilla enchantments in order to modify their max level.
     */
    void registerNewCraftEnchantments();
}

package com.willfp.ecoenchants.proxy.proxies;

public interface EcoCraftEnchantmentManagerProxy {
    /**
     * Re-Register new CraftEnchantments for all vanilla enchantments in order to modify their max level.
     */
    void registerNewCraftEnchantments();
}

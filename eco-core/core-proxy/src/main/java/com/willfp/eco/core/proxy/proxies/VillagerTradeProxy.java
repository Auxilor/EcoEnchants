package com.willfp.eco.core.proxy.proxies;

import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.inventory.MerchantRecipe;

public interface VillagerTradeProxy extends AbstractProxy {
    /**
     * Apply enchant display to the result of trades.
     *
     * @param merchantRecipe The recipe to modify.
     */
    void displayTradeEnchantments(MerchantRecipe merchantRecipe);
}

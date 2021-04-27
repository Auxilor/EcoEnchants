package com.willfp.ecoenchants.enchantments.itemtypes;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class VanillaEcoEnchantWrapper extends EcoEnchant {
    /**
     * Create a new Vanilla EcoEnchant Wrapper.
     *
     * @param enchantment The enchantment to wrap.
     */
    public VanillaEcoEnchantWrapper(@NotNull final Enchantment enchantment) {
        super(enchantment.getKey().getKey(), enchantment.isCursed() ? EnchantmentType.CURSE : EnchantmentType.NORMAL);
    }
}

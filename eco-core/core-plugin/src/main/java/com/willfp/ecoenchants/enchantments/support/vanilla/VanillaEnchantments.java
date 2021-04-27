package com.willfp.ecoenchants.enchantments.support.vanilla;

import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class VanillaEnchantments {
    /**
     * Get a map of all custom enchantment metadata.
     *
     * @return The map.
     */
    public Map<Enchantment, VanillaEnchantmentMetadata> getMetadataMap() {
        return new HashMap<>();
    }
}

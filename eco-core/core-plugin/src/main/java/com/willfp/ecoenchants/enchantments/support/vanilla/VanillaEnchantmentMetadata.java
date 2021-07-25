package com.willfp.ecoenchants.enchantments.support.vanilla;

import org.bukkit.NamespacedKey;

import java.util.Set;

public record VanillaEnchantmentMetadata(
        Integer maxLevel,
        Set<NamespacedKey> conflicts
) {
}

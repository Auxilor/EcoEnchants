package com.willfp.ecoenchants.vanilla

import org.bukkit.NamespacedKey

data class VanillaEnchantmentData(
    val maxLevel: Int?,
    val conflicts: Collection<NamespacedKey>?
)

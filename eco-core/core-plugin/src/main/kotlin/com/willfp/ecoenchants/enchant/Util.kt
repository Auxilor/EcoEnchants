package com.willfp.ecoenchants.enchant

import com.willfp.ecoenchants.enchant.impl.VanillaEcoEnchantLike
import com.willfp.ecoenchants.plugin
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment


private val ecoEnchantLikes = mutableMapOf<NamespacedKey, EcoEnchantLike>()

fun Enchantment.wrap(): EcoEnchantLike {
    if (this is EcoEnchant) {
        return this
    }

    return ecoEnchantLikes.getOrPut(this.key) {
        VanillaEcoEnchantLike(this, plugin)
    }
}

fun Enchantment.conflictsWithDeep(other: Enchantment): Boolean {
    return this.conflictsWith(other) || other.conflictsWith(this)
}

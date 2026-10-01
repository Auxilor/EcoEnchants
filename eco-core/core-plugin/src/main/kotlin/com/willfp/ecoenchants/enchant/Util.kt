package com.willfp.ecoenchants.enchant

import com.willfp.ecoenchants.enchant.impl.VanillaEcoEnchantLike
import com.willfp.ecoenchants.plugin
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import java.util.concurrent.ConcurrentHashMap

internal val ecoEnchantLikes = ConcurrentHashMap<NamespacedKey, EcoEnchantLike>()

fun Enchantment.wrap(): EcoEnchantLike {
    if (this is EcoEnchant) {
        return this
    }

    EcoEnchants.getByID(this.key.key)?.let { return it }

    return ecoEnchantLikes.computeIfAbsent(this.key) {
        VanillaEcoEnchantLike(this, plugin)
    }
}

fun Enchantment.conflictsWithDeep(other: Enchantment): Boolean {
    return this.conflictsWith(other) || other.conflictsWith(this)
}

internal fun Int.infiniteIfNegative() = if (this < 1) Int.MAX_VALUE else this

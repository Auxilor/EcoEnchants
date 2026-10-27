package com.willfp.ecoenchants.enchant

import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

/**
 * A single entry of the `required` section, in the format `id[:level] [remove]`.
 *
 * For example, `sharpness:2 remove` requires sharpness 2 or higher, and removes
 * sharpness from the item when the enchantment is applied.
 */
data class EnchantmentRequirement(
    val id: String,
    val level: Int = 1,
    val remove: Boolean = false
) {
    /**
     * The required enchantment, or null if it isn't registered.
     */
    val enchantment: Enchantment?
        @Suppress("DEPRECATION")
        get() = Enchantment.getByKey(NamespacedKey.minecraft(id))

    /**
     * Get if [enchantment] is the required enchantment.
     */
    fun matches(enchantment: Enchantment): Boolean =
        enchantment.key.key.equals(id, ignoreCase = true)

    /**
     * Get if [enchants] contain the required enchantment at the required level.
     */
    fun isMetBy(enchants: Map<Enchantment, Int>): Boolean =
        enchants.any { (enchantment, level) -> matches(enchantment) && level >= this.level }

    companion object {
        private const val REMOVE_FLAG = "remove"

        private val whitespace = Regex("\\s+")

        /**
         * Parse a requirement from a config [entry], or null if it is invalid.
         */
        fun parse(entry: String): EnchantmentRequirement? {
            val parts = entry.trim().split(whitespace)
            val idAndLevel = parts.first().split(":", limit = 2)

            val id = idAndLevel[0].lowercase()
            val level = idAndLevel.getOrNull(1)?.toIntOrNull() ?: if (idAndLevel.size == 1) 1 else 0
            val flags = parts.drop(1).map { it.lowercase() }

            val isValid = id.isNotEmpty() && level >= 1 && flags.all { it == REMOVE_FLAG }

            return if (isValid) EnchantmentRequirement(id, level, REMOVE_FLAG in flags) else null
        }
    }
}

/**
 * Remove every enchantment that [added] replaces from these enchantments.
 */
fun MutableMap<Enchantment, Int>.removeReplacedBy(added: Enchantment) {
    val ecoEnchant = added.ecoEnchant ?: return
    keys.removeIf { it != added && ecoEnchant.removesOnApply(it) }
}

/**
 * Remove every enchantment replaced by one of the newly [added] enchantments from this item.
 *
 * @return If any enchantments were removed.
 */
fun ItemStack.removeEnchantsReplacedBy(added: Collection<Enchantment>): Boolean {
    val replacing = added.mapNotNull { it.ecoEnchant }
        .filter { enchant -> enchant.requirements.any { it.remove } }

    val meta = itemMeta?.takeIf { replacing.isNotEmpty() } ?: return false
    val present = if (meta is EnchantmentStorageMeta) meta.storedEnchants.keys else meta.enchants.keys

    val toRemove = present.filter { enchantment ->
        enchantment !in added && replacing.any { it.removesOnApply(enchantment) }
    }

    for (enchantment in toRemove) {
        if (meta is EnchantmentStorageMeta) {
            meta.removeStoredEnchant(enchantment)
        } else {
            meta.removeEnchant(enchantment)
        }
    }

    if (toRemove.isNotEmpty()) {
        itemMeta = meta
    }

    return toRemove.isNotEmpty()
}

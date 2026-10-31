package com.willfp.ecoenchants.enchant

import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta

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
        get() = NamespacedKey.fromString(id)?.let { Enchantment.getByKey(it) }

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
 * Get if applying this enchantment would remove one that another of [enchants] needs to stay.
 */
fun EcoEnchant.breaksRequirementsOf(enchants: Collection<Enchantment>): Boolean {
    val replaced = enchants.filter { it != enchantment && removesOnApply(it) }

    return replaced.isNotEmpty() && enchants.any { other ->
        other != enchantment && other !in replaced && other.ecoEnchant?.requirements.orEmpty().any { requirement ->
            !requirement.remove && replaced.any(requirement::matches)
        }
    }
}

/**
 * Remove every enchantment replaced by one of the newly [added] enchantments from this item.
 *
 * Enchanted books are left as they are, so merging books never loses an enchantment.
 *
 * @return If any enchantments were removed.
 */
fun ItemStack.removeEnchantsReplacedBy(added: Collection<Enchantment>): Boolean {
    val replacing = added.replacingEnchants()
    val meta = (if (replacing.isNotEmpty()) itemMeta else null) ?: return false

    val removed = meta.removeReplaced(replacing)

    if (removed) {
        itemMeta = meta
    }

    return removed
}

/**
 * Remove every enchantment replaced by one of the newly [added] enchantments from this meta,
 * for when the meta is being written anyway. The meta of enchanted books is left as it is.
 *
 * @return If any enchantments were removed.
 */
fun ItemMeta.removeEnchantsReplacedBy(added: Collection<Enchantment>): Boolean =
    removeReplaced(added.replacingEnchants())

private fun ItemMeta.removeReplaced(replacing: List<EcoEnchant>): Boolean {
    val toRemove = if (this is EnchantmentStorageMeta) {
        emptyList()
    } else {
        enchants.keys.filter { enchantment ->
            replacing.any { it.enchantment != enchantment && it.removesOnApply(enchantment) }
        }
    }

    toRemove.forEach { removeEnchant(it) }

    return toRemove.isNotEmpty()
}

private fun Collection<Enchantment>.replacingEnchants(): List<EcoEnchant> =
    mapNotNull { it.ecoEnchant }.filter { enchant -> enchant.requirements.any { it.remove } }

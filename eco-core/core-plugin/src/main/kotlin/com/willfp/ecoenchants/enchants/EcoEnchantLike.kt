package com.willfp.ecoenchants.enchants

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.proxy.proxies.EcoCraftEnchantmentManagerProxy
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.util.*

interface EcoEnchantLike {
    val type: EnchantmentType
    val displayName: String
    val unformattedDisplayName: String
    val enchant: Enchantment
    val rarity: EnchantmentRarity

    fun getUnformattedDescription(level: Int): String

    // Includes all extra logic not found in vanilla canEnchantItem
    fun canEnchantItem(item: ItemStack): Boolean
}

private val ecoEnchantLikes = Caffeine.newBuilder()
    .build<NamespacedKey, EcoEnchantLike>()

fun Enchantment.wrap(): EcoEnchantLike {
    if (this is EcoEnchant) {
        return this
    }
    return ecoEnchantLikes.get(this.key) {
        VanillaEcoEnchantLike(this, EcoEnchantsPlugin.instance) // Jank
    }
}

fun Enchantment.conflictsWithDeep(other: Enchantment): Boolean {
    return this.conflictsWith(other) || other.conflictsWith(this)
}

class VanillaEcoEnchantLike(
    override val enchant: Enchantment,
    private val plugin: EcoEnchantsPlugin
) : EcoEnchantLike {
    override val type: EnchantmentType =
        EnchantmentTypes.getByID(plugin.vanillaEnchantsYml.getString("${enchant.key.key}.type"))
            ?: EnchantmentTypes.values().first()

    override val rarity: EnchantmentRarity =
        EnchantmentRarities.getByID(plugin.vanillaEnchantsYml.getString("${enchant.key.key}.rarity"))
            ?: EnchantmentRarities.values().first()

    override val displayName = plugin.vanillaEnchantsYml.getFormattedString("${enchant.key.key}.name")
    override val unformattedDisplayName = plugin.vanillaEnchantsYml.getString("${enchant.key.key}.name")

    override fun getUnformattedDescription(level: Int): String {
        return plugin.vanillaEnchantsYml.getString("${enchant.key.key}.description")
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        // Yes this code is copied from EcoEnchant, but I can't be bothered to abstract it properly
        if (
            item.fast().getEnchants(true).keys
                .map { it.wrap() }
                .count { it.type == this.type } >= this.type.limit
        ) {
            return false
        }

        if (item.fast().getEnchants(true).any { (enchant, _) -> enchant.conflictsWithDeep(this.enchant) }) {
            return false
        }

        if (item.type == Material.BOOK || item.type == Material.ENCHANTED_BOOK) {
            return true
        }

        return enchant.canEnchantItem(item)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is VanillaEcoEnchantLike) {
            return false
        }

        return this.enchant == other.enchant
    }

    override fun hashCode(): Int {
        return Objects.hash(this.enchant)
    }
}

fun registerVanillaEnchants(plugin: EcoEnchantsPlugin) {
    for (vanilla in plugin.vanillaEnchantsYml.getKeys(false)) {
        if (plugin.vanillaEnchantsYml.has("$vanilla.max-level")) {
            plugin.getProxy(EcoCraftEnchantmentManagerProxy::class.java).registerNewCraftEnchantment(
                Enchantment.getByKey(NamespacedKey.minecraft(vanilla))!!,
                plugin.vanillaEnchantsYml.getInt("$vanilla.max-level"),
                plugin.vanillaEnchantsYml.getStrings("$vanilla.conflicts").map { NamespacedKey.minecraft(it) }
            )
        }
    }
}

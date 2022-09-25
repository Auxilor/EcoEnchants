package com.willfp.ecoenchants.enchants

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.placeholder.InjectablePlaceholder
import com.willfp.eco.core.placeholder.PlaceholderInjectable
import com.willfp.eco.core.placeholder.StaticPlaceholder
import com.willfp.eco.util.NumberUtils
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.mechanics.infiniteIfNegative
import com.willfp.ecoenchants.proxy.proxies.EcoCraftEnchantmentManagerProxy
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import com.willfp.ecoenchants.vanilla.VanillaEnchantmentData
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.util.*

interface EcoEnchantLike {
    val config: Config
    val type: EnchantmentType
    val displayName: String
    val unformattedDisplayName: String
    val enchant: Enchantment
    val enchantmentRarity: EnchantmentRarity

    // Includes all extra logic not found in vanilla canEnchantItem
    fun canEnchantItem(item: ItemStack): Boolean

    // Method body goes here
    fun getUnformattedDescription(level: Int): String {
        // Fetch custom placeholders other than %placeholder%
        val uncompiledPlaceholders = config.getSubsection("placeholders").getKeys(false).associateWith {
            config.getString("placeholders.$it")
        }.toMutableMap()

        // Add %placeholder% placeholder in
        uncompiledPlaceholders["placeholder"] = config.getString("placeholder")

        // Evaluate each placeholder
        val placeholders = uncompiledPlaceholders.map { (id, expr) ->
            DescriptionPlaceholder(
                id,
                NumberUtils.evaluateExpression(
                    expr,
                    null,
                    object : PlaceholderInjectable {
                        override fun getPlaceholderInjections(): List<InjectablePlaceholder> {
                            return listOf(
                                StaticPlaceholder(
                                    "level",
                                ) { level.toString() }
                            )
                        }

                        override fun clearInjectedPlaceholders() {
                            // Do nothing
                        }
                    }
                )
            )
        }

        // Apply placeholders to description
        val rawDescription = config.getString("description")
        var description = rawDescription
        for (placeholder in placeholders) {
            description = description.replace("%${placeholder.id}%", NumberUtils.format(placeholder.value))
        }

        return description
    }
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
    override val config = plugin.vanillaEnchantsYml.getSubsection(enchant.key.key)

    override val type: EnchantmentType =
        EnchantmentTypes.getByID(plugin.vanillaEnchantsYml.getString("${enchant.key.key}.type"))
            ?: EnchantmentTypes.values().first()

    override val enchantmentRarity: EnchantmentRarity =
        EnchantmentRarities.getByID(plugin.vanillaEnchantsYml.getString("${enchant.key.key}.rarity"))
            ?: EnchantmentRarities.values().first()

    override val displayName = plugin.vanillaEnchantsYml.getFormattedString("${enchant.key.key}.name")
    override val unformattedDisplayName = plugin.vanillaEnchantsYml.getString("${enchant.key.key}.name")

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

        if (item.fast().getEnchants(true).size >= plugin.configYml.getInt("anvil.enchant-limit").infiniteIfNegative()) {
            return false
        }

        if (item.type == Material.ENCHANTED_BOOK) {
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

private val enchantmentOptions = arrayOf(
    "max-level",
    "conflicts"
)

fun registerVanillaEnchants(plugin: EcoEnchantsPlugin) {
    for (vanilla in plugin.vanillaEnchantsYml.getKeys(false)) {
        if (enchantmentOptions.any { plugin.vanillaEnchantsYml.has("$vanilla.$it") }) {
            plugin.getProxy(EcoCraftEnchantmentManagerProxy::class.java).registerNewCraftEnchantment(
                Enchantment.getByKey(NamespacedKey.minecraft(vanilla))!!,
                VanillaEnchantmentData(
                    plugin.vanillaEnchantsYml.getIntOrNull("$vanilla.max-level"),
                    plugin.vanillaEnchantsYml.getStringsOrNull("$vanilla.conflicts")
                        ?.map { NamespacedKey.minecraft(it) }
                )
            )
        }
    }
}

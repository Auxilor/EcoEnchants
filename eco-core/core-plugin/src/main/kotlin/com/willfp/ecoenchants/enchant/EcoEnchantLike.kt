package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.placeholder.InjectablePlaceholder
import com.willfp.eco.core.placeholder.PlaceholderInjectable
import com.willfp.eco.core.placeholder.StaticPlaceholder
import com.willfp.eco.core.placeholder.context.placeholderContext
import com.willfp.eco.util.NumberUtils
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.DescriptionPlaceholder
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.target.EnchantmentTarget
import com.willfp.ecoenchants.type.EnchantmentType
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface EcoEnchantLike {
    /**
     * The config.
     */
    val config: Config

    /**
     * The max enchantment level.
     */
    val maximumLevel: Int

    /**
     * The plugin.
     */
    val plugin: EcoEnchantsPlugin

    /**
     * The bukkit enchantment.
     */
    val enchantment: Enchantment

    /**
     * The raw display name.
     */
    val rawDisplayName: String

    /**
     * The (optional) name translation key
     */
    val nameTranslationKey: String?

    /**
     * The enchantment type.
     */
    val type: EnchantmentType

    /**
     * The enchantment rarity.
     */
    val enchantmentRarity: EnchantmentRarity

    /**
     * The enchantment targets.
     */
    val targets: Set<EnchantmentTarget>

    /**
     * Get if this enchantment can be applied to [item].
     *
     * Levels of [additionalEnchantments] aren't known, so they count as level 1
     * unless the item already has them at a higher level.
     *
     * This only delegates to the overload taking levels, so override that one instead:
     * overriding this has no effect on callers that pass levels.
     */
    fun canEnchantItem(
        item: ItemStack,
        additionalEnchantments: Collection<Enchantment> = emptyList()
    ): Boolean = canEnchantItem(
        item,
        if (additionalEnchantments.isEmpty()) emptyMap() else additionalEnchantments.associateWith { 1 }
    )

    /**
     * Get if this enchantment can be applied to [item], with [additionalEnchantments] mapped to their levels.
     *
     * Requirements are only checked when the enchantment isn't on the item yet, as enchantments
     * it replaces are gone afterwards. Enchantments this one replaces don't count towards
     * conflicts or limits, since they won't stay on the item, and it can't replace an enchantment
     * that another one on the item still needs. Enchantments replaced by [additionalEnchantments]
     * don't count towards anything, as they are going to be removed.
     */
    fun canEnchantItem(
        item: ItemStack,
        additionalEnchantments: Map<Enchantment, Int>
    ): Boolean {
        val itemEnchants = item.fast().getEnchants(true)
        val enchantsWithLevels = combineEnchants(itemEnchants, additionalEnchantments)

        if (this is EcoEnchant && this.enchantment !in itemEnchants && !this.canBeAddedTo(enchantsWithLevels)) {
            return false
        }

        val enchants = if (this is EcoEnchant && this.requirements.any { it.remove }) {
            enchantsWithLevels.keys.filterNot { this.removesOnApply(it) }
        } else {
            enchantsWithLevels.keys
        }

        if (enchants.count { it.wrap().type == this.type } >= this.type.limit) {
            return false
        }

        if (enchants.any { enchant -> enchant.conflictsWithDeep(this.enchantment) }) {
            return false
        }

        if (enchants.size >= plugin.configYml.getInt("anvil.enchant-limit").infiniteIfNegative()) {
            return false
        }

        if (item.type == Material.ENCHANTED_BOOK) {
            return true
        }

        return if (this is EcoEnchant) {
            this.targets.any { it.matches(item) }
        } else {
            enchantment.canEnchantItem(item)
        }
    }

    /**
     * Get the raw description for the enchantment.
     */
    fun getRawDescription(level: Int, player: Player?): List<String> {
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
                    placeholderContext(
                        player = player,
                        injectable = object : PlaceholderInjectable {
                            override fun getPlaceholderInjections(): List<InjectablePlaceholder> {
                                return listOf(
                                    StaticPlaceholder(
                                        "level",
                                    ) { level.toString() }
                                )
                            }

                            override fun addInjectablePlaceholder(p0: MutableIterable<InjectablePlaceholder>) {
                                // Do nothing
                            }

                            override fun clearInjectedPlaceholders() {
                                // Do nothing
                            }
                        }
                    )
                )
            )
        }

        // Handle both single-string and list format for description
        val rawDescription: List<String> = when (val descriptionValue = config.get("description")) {
            is String -> listOf(descriptionValue)
            is List<*> -> descriptionValue.filterIsInstance<String>()
            else -> emptyList()
        }

        // Apply placeholders to description
        return rawDescription.map { line ->
            var rawDescriptionFinal = line
            for (placeholder in placeholders) {
                rawDescriptionFinal = rawDescriptionFinal.replace("%${placeholder.id}%", NumberUtils.format(placeholder.value))
            }
            rawDescriptionFinal
        }
    }
}

private fun EcoEnchant.canBeAddedTo(enchants: Map<Enchantment, Int>): Boolean =
    hasRequiredEnchantments(enchants) && !breaksRequirementsOf(enchants.keys)

private fun combineEnchants(
    itemEnchants: Map<Enchantment, Int>,
    additionalEnchantments: Map<Enchantment, Int>
): Map<Enchantment, Int> {
    if (additionalEnchantments.isEmpty()) {
        return itemEnchants
    }

    val combined = itemEnchants.toMutableMap()

    for ((enchant, level) in additionalEnchantments) {
        combined.merge(enchant, level, ::maxOf)
    }

    for (enchant in additionalEnchantments.keys) {
        combined.removeReplacedBy(enchant)
    }

    return combined
}


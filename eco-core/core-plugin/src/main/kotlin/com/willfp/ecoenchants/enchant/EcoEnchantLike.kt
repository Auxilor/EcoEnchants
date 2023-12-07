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
import com.willfp.ecoenchants.mechanics.infiniteIfNegative
import com.willfp.ecoenchants.rarity.EnchantmentRarity
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
     * The enchantment type.
     */
    val type: EnchantmentType

    /**
     * The enchantment rarity.
     */
    val enchantmentRarity: EnchantmentRarity

    /**
     * Get if this enchantment can be applied to [item].
     */
    fun canEnchantItem(item: ItemStack): Boolean {
        if (
            item.fast().getEnchants(true).keys
                .map { it.wrap() }
                .count { it.type == this.type } >= this.type.limit
        ) {
            return false
        }

        if (item.fast().getEnchants(true).any { (enchant, _) -> enchant.conflictsWithDeep(this.enchantment) }) {
            return false
        }

        if (item.fast().getEnchants(true).size >= plugin.configYml.getInt("anvil.enchant-limit").infiniteIfNegative()) {
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
    fun getRawDescription(level: Int, player: Player?): String {
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

        // Apply placeholders to description
        val rawDescription = config.getString("description")
        var description = rawDescription
        for (placeholder in placeholders) {
            description = description.replace("%${placeholder.id}%", NumberUtils.format(placeholder.value))
        }

        return description
    }
}

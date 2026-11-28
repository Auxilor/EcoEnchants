package com.willfp.ecoenchants.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.type.EnchantmentTypes
import com.willfp.libreforge.ArgType
import com.willfp.libreforge.Dispatcher
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.ProvidedHolder
import com.willfp.libreforge.arguments
import com.willfp.libreforge.conditions.Condition
import com.willfp.libreforge.get
import com.willfp.libreforge.getStrings
import com.willfp.libreforge.slot.SlotTypes
import org.bukkit.entity.LivingEntity

object ConditionHasEnchantType : Condition<NoCompileData>("has_enchant_type") {
    override val description = "Passes when an item in the slot has enough enchantments of the given type(s)."

    override val categories = setOf("inventory")

    override val additionalInfo = listOf(
        "Types are the IDs in types.yml, such as normal, curse, spell or special.",
        "The level can be a single integer or a range (e.g. 1-3)."
    )

    override val arguments = arguments {
        require(
            listOf("slot", "slots"),
            "You must specify the slot(s)!",
            description = "The equipment slot(s) to check.",
            type = ArgType.STRING_LIST,
            example = listOf("mainhand")
        )
        require(
            listOf("type", "types"),
            "You must specify the type(s)!",
            description = "The enchantment type(s) to count.",
            type = ArgType.STRING_LIST,
            example = listOf("curse")
        )
        optional(
            "amount",
            description = "The minimum number of matching enchantments on one item. Defaults to 1.",
            type = ArgType.INT,
            default = "1"
        )
        optional(
            "level",
            description = "The level or level range a matching enchantment must have.",
            type = ArgType.STRING,
            example = "1-3"
        )
    }

    override fun isMet(
        dispatcher: Dispatcher<*>,
        config: Config,
        holder: ProvidedHolder,
        compileData: NoCompileData
    ): Boolean {
        val livingEntity = dispatcher.get<LivingEntity>() ?: return false
        val types = config.getStrings(pluralPath = "types", singularPath = "type")
            .mapNotNull { EnchantmentTypes[it] }
            .toSet()
        val amount = if (config.has("amount")) config.getInt("amount") else 1
        val levels = config.getStringOrNull("level")?.let { parseLevelRange(it) }

        return config.getStrings(pluralPath = "slots", singularPath = "slot")
            .mapNotNull { SlotTypes[it] }
            .flatMap { it.getItems(livingEntity) }
            .any { item ->
                item.enchantments.count { (enchantment, level) ->
                    enchantment.wrap().type in types && (levels == null || level in levels)
                } >= amount
            }
    }

    private fun parseLevelRange(level: String): IntRange {
        level.toIntOrNull()?.let { return it..it }

        val bounds = level.split("-").mapNotNull { it.trim().toIntOrNull() }

        return if (bounds.size == 2 && bounds[0] <= bounds[1]) bounds[0]..bounds[1] else IntRange.EMPTY
    }
}

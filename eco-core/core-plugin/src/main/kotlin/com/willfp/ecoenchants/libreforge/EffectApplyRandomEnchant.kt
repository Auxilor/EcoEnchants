package com.willfp.ecoenchants.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.libreforge.ArgType
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.Material
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object EffectApplyRandomEnchant : Effect<NoCompileData>("apply_random_enchant") {
    override val description = "Applies a random enchantment to the triggering item, " +
        "optionally restricted by type, rarity, or enchant list."

    override val categories = setOf("inventory")

    override val additionalInfo = listOf(
        "If types, rarities, and enchants are all left empty, any enchantment can be chosen.",
        "If allow_unsafe is false, only enchantments that the item can normally receive will be chosen."
    )

    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.ITEM
    )

    override val arguments = arguments {
        optional(
            "types",
            description = "If not empty, only enchantments belonging to these enchantment types can be chosen.",
            type = ArgType.STRING_LIST,
            default = "[]"
        )
        optional(
            "rarities",
            description = "If not empty, only enchantments with these rarities can be chosen.",
            type = ArgType.STRING_LIST,
            default = "[]"
        )
        optional(
            "enchants",
            description = "If not empty, only enchantments with these IDs can be chosen.",
            type = ArgType.ENCHANTMENT_LIST,
            default = "[]"
        )
        optional(
            "allow_unsafe",
            description = "If true, enchantments that wouldn't normally be allowed on the item " +
                "(e.g. exceeding level caps or conflicting enchantments) can still be chosen.",
            type = ArgType.BOOLEAN,
            default = "false"
        )
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false
        val item = data.item ?: return false

        val types = config.getStrings("types").map { it.lowercase() }.toSet()
        val rarities = config.getStrings("rarities").map { it.lowercase() }.toSet()
        val enchants = config.getStrings("enchants").map { it.lowercase() }.toSet()

        val allowUnsafe = config.getBool("allow_unsafe")

        val enchant = EcoEnchants.values()
            .filter { types.isEmpty() || it.type.id.lowercase() in types }
            .filter { rarities.isEmpty() || it.enchantmentRarity.id.lowercase() in rarities }
            .filter { enchants.isEmpty() || it.id.lowercase() in enchants }
            .filter { allowUnsafe || it.canEnchantItem(item) }
            .randomOrNull() ?: return false

        val level = NumberUtils.randInt(1, maxOf(1, enchant.maximumLevel))

        val meta = item.itemMeta ?: return false

        if (item.type == Material.ENCHANTED_BOOK && meta is EnchantmentStorageMeta) {
            meta.addStoredEnchant(enchant.enchantment, level, true)
        } else {
            meta.addEnchant(enchant.enchantment, level, true)
        }

        item.itemMeta = meta
        return true
    }
}

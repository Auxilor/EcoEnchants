package com.willfp.ecoenchants.display

import com.willfp.eco.core.cache.EcoCache
import com.willfp.eco.core.placeholder.context.placeholderContext
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.formatEco
import com.willfp.eco.util.toComponent
import com.willfp.ecoenchants.enchant.EcoEnchantLike
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

// This is an object to be able to invalidate the cache on reload
object DisplayCache {
    val nameCache: EcoCache<DisplayableEnchant, String> = EcoCache.builder<DisplayableEnchant, String>()
        .build()

    val descriptionCache: EcoCache<DisplayableEnchant, List<String>> = EcoCache.builder<DisplayableEnchant, List<String>>()
        .build()

    internal fun reload() {
        nameCache.invalidateAll()
        descriptionCache.invalidateAll()
    }
}

data class DisplayableEnchant(
    val enchant: EcoEnchantLike,
    val level: Int,
    val showNotMet: Boolean = false
)

@JvmOverloads
fun EcoEnchantLike.getFormattedName(
    level: Int,
    showNotMet: Boolean = false
): String {
    return DisplayCache.nameCache.get(DisplayableEnchant(this, level, showNotMet)) {
        val numerals = plugin.configYml.getBool("display.numerals.enabled") &&
                level <= plugin.configYml.getInt("display.numerals.threshold")

        val typeFormat = this.type.format
        val name = this.rawDisplayName
        val number = if (numerals) NumberUtils.toNumeral(level) else level.toString()
        val dontShowNumber = (level == 1 && this.maximumLevel == 1) || level < 1

        val notMetFormat = if (showNotMet) plugin.configYml.getString("display.not-met.format") else ""

        if (plugin.configYml.getBool("display.above-max-level.enabled") && level > this.maximumLevel) {
            val format = plugin.configYml.getString("display.above-max-level.format")
            val levelOnly = plugin.configYml.getBool("display.above-max-level.level-only")

            if (levelOnly) {
                StringUtils.format("$typeFormat$notMetFormat$name $format$number")
            } else {
                StringUtils.format("$format$notMetFormat$name $number")
            }
        } else {
            if (dontShowNumber) {
                StringUtils.format("$typeFormat$notMetFormat$name")
            } else {
                StringUtils.format("$typeFormat$notMetFormat$name $number")
            }
        }
    }
}

// Vanilla always builds the enchantment level numeral as an unstyled sibling appended to this
// component, inheriting whatever color sits on its root. The legacy serializer used by
// toComponent() wraps colored text in an empty root with the actual color on a child, so vanilla's
// numeral inherits nothing and renders grey. Hoisting the color onto the root fixes that.
fun EcoEnchantLike.getFormattedNameComponent(level: Int, showNotMet: Boolean = false): Component {
    val name = this.getFormattedName(level, showNotMet).toComponent()

    if (name.color() != null) {
        return name
    }

    val childColor = name.children().firstOrNull()?.color() ?: return name

    return name.color(childColor)
}

private val resetTags = arrayOf(
    "<reset>",
    "&r",
    "§r"
)

fun EcoEnchantLike.getFormattedDescription(level: Int, player: Player? = null): List<String> {
    return DisplayCache.descriptionCache.get(DisplayableEnchant(this, level)) {
        val descriptionFormat = plugin.configYml.getString("display.descriptions.format")
        val wrap = plugin.configYml.getInt("display.descriptions.word-wrap")
        val rawDescriptions = this.getRawDescription(level, player)

        // Combine all transformations in one pass
        rawDescriptions.flatMap { line ->
            // Apply the description format and reset tags, then apply word wrapping
            var formattedLine = descriptionFormat + line
            resetTags.forEach { tag ->
                formattedLine = formattedLine.replace(tag, tag + descriptionFormat)
            }

            // Apply word wrapping after all formatting
            StringUtils.lineWrap(formattedLine.formatEco(placeholderContext(
                injectable = this.config)), wrap)
        }
    }
}

// Java backwards compatibility
fun EcoEnchantLike.getFormattedDescription(level: Int): List<String> = getFormattedDescription(level, null)

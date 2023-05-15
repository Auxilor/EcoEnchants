package com.willfp.ecoenchants.display

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.placeholder.context.placeholderContext
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.formatEco
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchantLike

// This is an object to be able to invalidate the cache on reload
object DisplayCache {
    val nameCache: Cache<DisplayableEnchant, String> = Caffeine.newBuilder()
        .build()

    val descriptionCache: Cache<DisplayableEnchant, List<String>> = Caffeine.newBuilder()
        .build()

    internal fun reload() {
        nameCache.invalidateAll()
        descriptionCache.invalidateAll()
    }
}

data class DisplayableEnchant(
    val enchant: EcoEnchantLike,
    val level: Int
)

@JvmOverloads
fun EcoEnchantLike.getFormattedName(
    level: Int,
    showNotMet: Boolean = false
): String {
    val plugin = EcoEnchantsPlugin.instance

    return DisplayCache.nameCache.get(DisplayableEnchant(this, level)) {
        val numerals = plugin.configYml.getBool("display.numerals.enabled") &&
                level <= plugin.configYml.getInt("display.numerals.threshold")

        val typeFormat = this.type.format
        val name = this.unformattedDisplayName
        val number = if (numerals) NumberUtils.toNumeral(level) else level.toString()
        val dontShowNumber = (level == 1 && this.enchant.maxLevel == 1) || level < 1

        val notMetFormat = if (showNotMet) plugin.configYml.getString("display.not-met.format") else ""

        if (plugin.configYml.getBool("display.above-max-level.enabled") && level > this.enchant.maxLevel) {
            val format = plugin.configYml.getString("display.above-max-level.format")
            val levelOnly = plugin.configYml.getBool("display.above-max-level.level-only")

            if (levelOnly) {
                StringUtils.format("$notMetFormat$typeFormat$name $format$number")
            } else {
                StringUtils.format("$notMetFormat$format$name $number")
            }
        } else {
            if (dontShowNumber) {
                StringUtils.format("$notMetFormat$typeFormat$name")
            } else {
                StringUtils.format("$notMetFormat$typeFormat$name $number")
            }
        }
    }
}

private val resetTags = arrayOf(
    "<reset>",
    "&r",
    "§r"
)

fun EcoEnchantLike.getFormattedDescription(level: Int): List<String> {
    val plugin = EcoEnchantsPlugin.instance

    return DisplayCache.descriptionCache.get(DisplayableEnchant(this, level)) {
        val descriptionFormat = plugin.configYml.getString("display.descriptions.format")
        val wrap = plugin.configYml.getInt("display.descriptions.word-wrap")

        var description = descriptionFormat + this.getUnformattedDescription(level)

        // Replace reset tags with description format
        for (tag in resetTags) {
            description = description.replace(tag, tag + descriptionFormat)
        }

        StringUtils.lineWrap(description.formatEco(placeholderContext(
            injectable = this.config
        )), wrap)
    }
}

package com.willfp.ecoenchants.display

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.placeholder.context.placeholderContext
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.formatEco
import com.willfp.ecoenchants.enchant.EcoEnchantLike
import org.bukkit.entity.Player

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

private val resetTags = arrayOf(
    "<reset>",
    "&r",
    "§r"
)

fun EcoEnchantLike.getFormattedDescription(level: Int, player: Player? = null): List<String> {
    return DisplayCache.descriptionCache.get(DisplayableEnchant(this, level)) {
        val descriptionFormat = plugin.configYml.getString("display.descriptions.format")
        val wrap = plugin.configYml.getInt("display.descriptions.word-wrap")

        var description = descriptionFormat + this.getRawDescription(level, player)

        // Replace reset tags with description format
        for (tag in resetTags) {
            description = description.replace(tag, tag + descriptionFormat)
        }

        StringUtils.lineWrap(description.formatEco(placeholderContext(
            injectable = this.config
        )), wrap)
    }
}

// Java backwards compatibility
fun EcoEnchantLike.getFormattedDescription(level: Int): List<String> = getFormattedDescription(level, null)

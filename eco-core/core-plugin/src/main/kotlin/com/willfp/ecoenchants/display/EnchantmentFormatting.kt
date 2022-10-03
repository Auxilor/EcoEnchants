package com.willfp.ecoenchants.display

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.integrations.placeholder.PlaceholderManager
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.StringUtils
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchantLike
import org.apache.commons.lang.WordUtils
import org.bukkit.ChatColor

// This is an object to be able to invalidate the cache on reload
object DisplayCache {
    val nameCache: Cache<DisplayableEnchant, String> = Caffeine.newBuilder()
        .build()

    val descriptionCache: Cache<DisplayableEnchant, List<String>> = Caffeine.newBuilder()
        .build()

    @JvmStatic
    @ConfigUpdater
    fun onReload() {
        nameCache.invalidateAll()
        descriptionCache.invalidateAll()
    }
}

data class DisplayableEnchant(
    val enchant: EcoEnchantLike,
    val level: Int
)

fun EcoEnchantLike.getFormattedName(level: Int): String {
    val plugin = EcoEnchantsPlugin.instance

    return DisplayCache.nameCache.get(DisplayableEnchant(this, level)) {
        val numerals = plugin.configYml.getBool("display.numerals.enabled") &&
                level <= plugin.configYml.getInt("display.numerals.threshold")

        val typeFormat = this.type.format
        val name = this.unformattedDisplayName
        val number = if (numerals) NumberUtils.toNumeral(level) else level.toString()
        val dontShowNumber = (level == 1 && this.enchant.maxLevel == 1) || level < 1

        if (plugin.configYml.getBool("display.above-max-level.enabled") && level > this.enchant.maxLevel) {
            val format = plugin.configYml.getString("display.above-max-level.format")
            val levelOnly = plugin.configYml.getBool("display.above-max-level.level-only")

            if (levelOnly) {
                StringUtils.format("$typeFormat$name $format$number")
            } else {
                StringUtils.format("$format$name $number")
            }
        } else {
            if (dontShowNumber) {
                StringUtils.format("$typeFormat$name")
            } else {
                StringUtils.format("$typeFormat$name $number")
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

        var description = this.getUnformattedDescription(level)

        /*
         Essentially, to work with word wrapping (and colored placeholders),
         placeholders are translated first, then replaced with a unique mock
         placeholder the length of the translated value, and then swapped back in
         at the end.
         */
        val placeholders = PlaceholderManager.findPlaceholdersIn(description)
        val mockPlaceholderMap = mutableMapOf<Int, String>()
        val mockPlaceholderIDs = mutableMapOf<String, Int>()

        var i = 1
        for (placeholder in placeholders) {
            val translated = PlaceholderManager.translatePlaceholders(placeholder, null)
            mockPlaceholderMap[i] = translated

            val length = ChatColor.stripColor(translated)!!.length
            val toReplaceWith = "[${i.toString().repeat((length - 2).coerceAtLeast(1))}]"
            mockPlaceholderIDs[toReplaceWith] = i
            description = description.replace(placeholder, toReplaceWith)
            i++
        }

        /** Replace reset tags with description format. */
        for (tag in resetTags) {
            description = description.replace(tag, tag + descriptionFormat)
        }

        /** Wrap the lines. */
        val wrapped = WordUtils.wrap(description, wrap, "\n", false)
            .lines()
            .map { StringUtils.format(descriptionFormat + it) }

        /** Swap back in the original placeholder. */
        wrapped.map {
            var string = it
            for ((mock, id) in mockPlaceholderIDs) {
                string = string.replace(mock, mockPlaceholderMap[id] ?: "")
            }
            string
        }
    }
}

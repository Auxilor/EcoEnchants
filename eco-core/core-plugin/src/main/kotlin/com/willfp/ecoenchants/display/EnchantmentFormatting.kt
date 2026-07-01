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
        val rawDescriptions = this.getRawDescription(level, player)

        // Combine all transformations in one pass
        rawDescriptions.flatMap { line ->
            // Apply the description format and reset tags, then apply word wrapping
            var formattedLine = descriptionFormat + line
            resetTags.forEach { tag ->
                formattedLine = formattedLine.replace(tag, tag + descriptionFormat)
            }

            // Apply word wrapping after all formatting, without counting color codes.
            formattedLine.formatEco(placeholderContext(
                injectable = this.config
            )).lineWrapIgnoringFormatting(wrap)
        }
    }
}

// Java backwards compatibility
fun EcoEnchantLike.getFormattedDescription(level: Int): List<String> = getFormattedDescription(level, null)

private val wrapTokenPattern = Regex("""\s+|\S+""")
private const val LEGACY_COLOR_CHAR = '§'
private const val AMPERSAND_COLOR_CHAR = '&'
private const val HEX_FORMAT_LENGTH = 14
private const val LEGACY_FORMAT_LENGTH = 2
private val colorCodes = setOf(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f'
)
private val formatCodes = setOf('k', 'l', 'm', 'n', 'o')
private val resetCode = 'r'

private fun String.lineWrapIgnoringFormatting(width: Int): List<String> {
    if (width <= 0) {
        return listOf(this)
    }

    val lines = mutableListOf<String>()
    var current = StringBuilder()
    var currentWidth = 0
    var activeFormatting = ""
    var pendingWhitespace = ""

    fun emitLine() {
        lines += current.toString().trimEnd()
        current = StringBuilder(activeFormatting)
        currentWidth = 0
    }

    fun appendText(text: String, splitLongToken: Boolean) {
        var index = 0
        while (index < text.length) {
            val formatLength = text.formatLengthAt(index)
            if (formatLength > 0) {
                val format = text.substring(index, index + formatLength)
                current.append(format)
                activeFormatting = activeFormatting.applyFormat(format)
                index += formatLength
                continue
            }

            if (splitLongToken && currentWidth >= width && currentWidth > 0) {
                emitLine()
            }

            current.append(text[index])
            currentWidth++
            index++
        }
    }

    for (token in wrapTokenPattern.findAll(this).map { it.value }) {
        if (token.isBlank()) {
            if (currentWidth > 0) {
                pendingWhitespace = token
            }
            continue
        }

        val leadingWhitespace = if (currentWidth > 0) pendingWhitespace else ""
        val tokenWithWhitespace = leadingWhitespace + token
        val visibleLength = tokenWithWhitespace.visibleLength()

        if (currentWidth > 0 && currentWidth + visibleLength > width) {
            emitLine()
            appendText(token, token.visibleLength() > width)
        } else {
            appendText(tokenWithWhitespace, visibleLength > width)
        }

        pendingWhitespace = ""
    }

    if (current.isNotEmpty()) {
        lines += current.toString().trimEnd()
    }

    return lines
}

private fun String.visibleLength(): Int {
    var length = 0
    var index = 0

    while (index < this.length) {
        val formatLength = this.formatLengthAt(index)
        if (formatLength > 0) {
            index += formatLength
            continue
        }

        length++
        index++
    }

    return length
}

private fun String.formatLengthAt(index: Int): Int {
    if (index + 1 >= this.length) {
        return 0
    }

    val marker = this[index]
    if (marker != LEGACY_COLOR_CHAR && marker != AMPERSAND_COLOR_CHAR) {
        return 0
    }

    val code = this[index + 1].lowercaseChar()
    if (code == 'x' && this.hasHexFormatAt(index, marker)) {
        return HEX_FORMAT_LENGTH
    }

    if (code in colorCodes || code in formatCodes || code == resetCode) {
        return LEGACY_FORMAT_LENGTH
    }

    return 0
}

private fun String.hasHexFormatAt(index: Int, marker: Char): Boolean {
    if (index + HEX_FORMAT_LENGTH > this.length) {
        return false
    }

    for (offset in 2 until HEX_FORMAT_LENGTH step 2) {
        if (this[index + offset] != marker || !this[index + offset + 1].isHexDigit()) {
            return false
        }
    }

    return true
}

private fun Char.isHexDigit(): Boolean =
    this in '0'..'9' || this in 'a'..'f' || this in 'A'..'F'

private fun String.applyFormat(format: String): String {
    val code = format.getOrNull(1)?.lowercaseChar() ?: return this

    return when {
        code == resetCode -> ""
        code == 'x' || code in colorCodes -> format
        code in formatCodes -> if (this.contains(format)) this else this + format
        else -> this
    }
}

package com.willfp.ecoenchants

private val legacyFormattingPattern = Regex("(?i)\\u00A7[0-9A-FK-ORX]")

internal fun String.stripLegacyFormatting(): String =
    legacyFormattingPattern.replace(this, "")

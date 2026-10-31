package com.willfp.ecoenchants.enchant

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class EnchantmentRequirementTests {
    @Test
    fun plainIdUsesDefaults() {
        assertEquals(EnchantmentRequirement("sharpness", 1, false), EnchantmentRequirement.parse("sharpness"))
    }

    @Test
    fun levelIsParsed() {
        assertEquals(EnchantmentRequirement("sharpness", 2, false), EnchantmentRequirement.parse("sharpness:2"))
    }

    @Test
    fun removeFlagIsParsed() {
        assertEquals(EnchantmentRequirement("sharpness", 1, true), EnchantmentRequirement.parse("sharpness remove"))
    }

    @Test
    fun levelAndRemoveAreParsed() {
        assertEquals(EnchantmentRequirement("sharpness", 2, true), EnchantmentRequirement.parse("sharpness:2 remove"))
    }

    @Test
    fun caseAndSpacingAreIgnored() {
        val parsed = EnchantmentRequirement.parse("  SHARPNESS:2   REMOVE ")

        assertEquals(EnchantmentRequirement("sharpness", 2, true), parsed)
    }

    @Test
    fun levelBelowOneIsInvalid() {
        assertNull(EnchantmentRequirement.parse("sharpness:0"), "zero")
        assertNull(EnchantmentRequirement.parse("sharpness:-1"), "negative")
    }

    @Test
    fun nonNumericLevelIsInvalid() {
        assertNull(EnchantmentRequirement.parse("sharpness:abc"), "letters")
        assertNull(EnchantmentRequirement.parse("sharpness:"), "empty level")
    }

    @Test
    fun unknownFlagIsInvalid() {
        assertNull(EnchantmentRequirement.parse("sharpness remvoe"))
    }

    @Test
    fun emptyIdIsInvalid() {
        assertNull(EnchantmentRequirement.parse(""), "empty entry")
        assertNull(EnchantmentRequirement.parse(":2"), "level only")
    }
}

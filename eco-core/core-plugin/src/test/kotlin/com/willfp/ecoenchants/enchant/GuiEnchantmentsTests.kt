package com.willfp.ecoenchants.enchant

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class GuiEnchantmentsTests {
    private val vanilla = mapOf("protection" to "vanilla")

    @Test
    fun customWinsOverVanilla() {
        assertEquals("custom", resolveByName("protection", { "custom" }, vanilla, true))
    }

    @Test
    fun fallsBackToVanilla() {
        assertEquals("vanilla", resolveByName("protection", { null }, vanilla, true))
    }

    @Test
    fun caseInsensitive() {
        assertEquals("vanilla", resolveByName("PROTECTION", { null }, vanilla, true))
    }

    @Test
    fun noVanillaWhenDisabled() {
        assertNull(resolveByName("protection", { null }, vanilla, false))
    }
}

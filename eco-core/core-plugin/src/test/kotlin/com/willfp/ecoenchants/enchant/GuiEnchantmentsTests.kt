package com.willfp.ecoenchants.enchant

import com.willfp.ecoenchants.FakeConfig
import org.bukkit.NamespacedKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Test
    fun datapackKeyNotListed() {
        val sections = FakeConfig(mapOf("sharpness" to "section"))
        assertTrue(isListedVanilla(NamespacedKey.minecraft("sharpness"), sections))
        assertFalse(isListedVanilla(NamespacedKey("mypack", "sharpness"), sections))
        assertFalse(isListedVanilla(NamespacedKey.minecraft("lunge"), sections))
    }
}

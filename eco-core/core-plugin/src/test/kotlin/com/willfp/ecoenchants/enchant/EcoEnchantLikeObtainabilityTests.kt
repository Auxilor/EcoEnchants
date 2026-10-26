package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.FakeConfig
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.target.EnchantmentTarget
import com.willfp.ecoenchants.type.EnchantmentType
import org.bukkit.enchantments.Enchantment
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private fun fakeEnchant(values: Map<String, Any?> = emptyMap()): EcoEnchantLike =
    object : EcoEnchantLike {
        override val config: Config = FakeConfig(values)
        override val maximumLevel: Int = 5
        override val plugin: EcoEnchantsPlugin get() = throw UnsupportedOperationException("not used in these tests")
        override val enchantment: Enchantment get() = throw UnsupportedOperationException("not used in these tests")
        override val rawDisplayName: String = "fake"
        override val nameTranslationKey: String? = null
        override val type: EnchantmentType get() = throw UnsupportedOperationException("not used in these tests")
        override val enchantmentRarity: EnchantmentRarity get() = throw UnsupportedOperationException("not used in these tests")
        override val targets: Set<EnchantmentTarget> = emptySet()
    }

internal class EcoEnchantLikeObtainabilityTests {
    @Test
    fun missingKeysAreFalse() {
        val enchant = fakeEnchant()
        assertFalse(enchant.isObtainableThroughEnchanting)
        assertFalse(enchant.isObtainableThroughTrading)
        assertFalse(enchant.isObtainableThroughDiscovery)
        assertFalse(enchant.isHiddenFromGui)
        DiscoveryType.entries.forEach { assertFalse(enchant.isObtainableThrough(it)) }
    }

    @Test
    fun booleanDiscoverableAppliesToAll() {
        val enchant = fakeEnchant(mapOf("discoverable" to true))
        DiscoveryType.entries.forEach { assertTrue(enchant.isObtainableThrough(it)) }
    }

    @Test
    fun sectionDiscoverablePerType() {
        val enchant = fakeEnchant(mapOf("discoverable.chests" to true, "discoverable.fishing" to false))
        assertTrue(enchant.isObtainableThrough(DiscoveryType.CHESTS))
        assertFalse(enchant.isObtainableThrough(DiscoveryType.FISHING))
        assertFalse(enchant.isObtainableThrough(DiscoveryType.MOB_DROPS))
        assertTrue(enchant.isObtainableThroughDiscovery)
    }

    @Test
    fun readsTradeableAndEnchantable() {
        val enchant = fakeEnchant(mapOf("tradeable" to true, "enchantable" to false))
        assertTrue(enchant.isObtainableThroughTrading)
        assertFalse(enchant.isObtainableThroughEnchanting)
    }

    @Test
    fun readsHiddenFromGui() {
        assertTrue(fakeEnchant(mapOf("hide-from-enchantgui" to true)).isHiddenFromGui)
    }
}

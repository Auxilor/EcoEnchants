package com.willfp.ecoenchants.dragdrop

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.FakeConfig
import com.willfp.ecoenchants.enchant.EcoEnchantLike
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.target.EnchantmentTarget
import com.willfp.ecoenchants.type.EnchantmentType
import org.bukkit.enchantments.Enchantment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private fun fakeEnchant(
    priceLevelMultiplier: String? = null,
    enabled: Boolean? = null
): EcoEnchantLike {
    val values = mutableMapOf<String, Any?>()
    priceLevelMultiplier?.let { values["drag-and-drop.price-level-multiplier"] = it }
    enabled?.let { values["drag-and-drop.enabled"] = it }

    val config = FakeConfig(values)

    return object : EcoEnchantLike {
        override val config: Config = config
        override val maximumLevel: Int = 5
        override val plugin: EcoEnchantsPlugin get() = throw UnsupportedOperationException("not used in these tests")
        override val enchantment: Enchantment get() = throw UnsupportedOperationException("not used in these tests")
        override val rawDisplayName: String = "fake"
        override val nameTranslationKey: String? = null
        override val type: EnchantmentType get() = throw UnsupportedOperationException("not used in these tests")
        override val enchantmentRarity: EnchantmentRarity get() = throw UnsupportedOperationException("not used in these tests")
        override val targets: Set<EnchantmentTarget> = emptySet()
    }
}

internal class DragAndDropPricingTests {
    @Test
    fun multiplierDefaultsToOneWhenKeyMissing() {
        val enchant = fakeEnchant(priceLevelMultiplier = null)
        assertEquals(1.0, enchant.dragAndDropPriceMultiplier(5))
    }

    @Test
    fun disabledByDefaultWhenKeyMissing() {
        val enchant = fakeEnchant(enabled = null)
        assertFalse(enchant.isDragAndDropEnabled())
    }

    @Test
    fun enabledWhenKeyTrue() {
        val enchant = fakeEnchant(enabled = true)
        assertTrue(enchant.isDragAndDropEnabled())
    }
}

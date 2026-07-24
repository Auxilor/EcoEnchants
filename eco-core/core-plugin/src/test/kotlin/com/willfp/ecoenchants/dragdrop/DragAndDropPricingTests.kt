package com.willfp.ecoenchants.dragdrop

import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.StringUtils
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.EcoEnchantLike
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import org.bukkit.enchantments.Enchantment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private class FakeConfig(private val values: Map<String, Any?> = emptyMap()) : Config {
    override fun toPlaintext(): String = ""

    override fun has(path: String): Boolean = values.containsKey(path)

    override fun getKeys(deep: Boolean): List<String> = values.keys.toList()

    override fun get(path: String): Any? = values[path]

    override fun set(path: String, `object`: Any?) {
        throw UnsupportedOperationException("FakeConfig is read-only")
    }

    override fun getSubsectionOrNull(path: String): Config? {
        val prefix = "$path."
        val nested = values.filterKeys { it.startsWith(prefix) }
            .mapKeys { (key, _) -> key.removePrefix(prefix) }
        return if (nested.isEmpty()) null else FakeConfig(nested)
    }

    override fun getIntOrNull(path: String): Int? = values[path] as? Int

    override fun getIntsOrNull(path: String): List<Int>? = null

    override fun getBoolOrNull(path: String): Boolean? = values[path] as? Boolean

    override fun getBoolsOrNull(path: String): List<Boolean>? = null

    override fun getStringOrNull(path: String, format: Boolean, option: StringUtils.FormatOption): String? {
        return values[path] as? String
    }

    override fun getStringsOrNull(path: String, format: Boolean, option: StringUtils.FormatOption): List<String>? {
        return null
    }

    override fun getDoubleOrNull(path: String): Double? = values[path] as? Double

    override fun getDoublesOrNull(path: String): List<Double>? = null

    override fun getSubsectionsOrNull(path: String): List<Config>? = null

    override fun getType(): ConfigType = ConfigType.JSON

    override fun clone(): Config = FakeConfig(values)
}

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
        override val type: EnchantmentType get() = throw UnsupportedOperationException("not used in these tests")
        override val enchantmentRarity: EnchantmentRarity get() = throw UnsupportedOperationException("not used in these tests")
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

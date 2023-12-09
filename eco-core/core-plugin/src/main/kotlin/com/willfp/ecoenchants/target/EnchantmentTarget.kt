package com.willfp.ecoenchants.target

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.eco.core.registry.Registrable
import com.willfp.ecoenchants.plugin
import com.willfp.libreforge.slot.SlotType
import com.willfp.libreforge.slot.SlotTypes
import com.willfp.libreforge.slot.impl.SlotTypeAny
import org.bukkit.inventory.ItemStack
import java.util.Objects

interface EnchantmentTarget : Registrable {
    val id: String
    val displayName: String
    val slot: SlotType
    val items: List<TestableItem>

    fun matches(itemStack: ItemStack): Boolean {
        for (item in items) {
            if (item.matches(itemStack)) {
                return true
            }
        }
        return false
    }

    override fun getID(): String {
        return this.id
    }
}

class ConfiguredEnchantmentTarget(
    config: Config
) : EnchantmentTarget {
    override val id = config.getString("id")
    override val displayName = config.getFormattedString("display-name")

    override val slot = SlotTypes[config.getString("slot")] ?:
    throw IllegalArgumentException("Invalid slot type: ${config.getString("slot")}, options are ${SlotTypes.values().map { it.id }}")

    override val items = config.getStrings("items")
        .map { Items.lookup(it) }
        .filterNot { it is EmptyTestableItem }

    override fun equals(other: Any?): Boolean {
        if (other !is EnchantmentTarget) {
            return false
        }

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }
}

internal object AllEnchantmentTarget : EnchantmentTarget {
    override val id = "all"
    override val displayName = plugin.langYml.getFormattedString("all")
    override val slot = SlotTypeAny
    override var items = emptyList<TestableItem>()
        private set

    fun updateItems() {
        items = EnchantmentTargets.values()
            .filterNot { it == this }
            .flatMap { it.items }
    }

    override fun equals(other: Any?): Boolean {
        return other is AllEnchantmentTarget
    }
}

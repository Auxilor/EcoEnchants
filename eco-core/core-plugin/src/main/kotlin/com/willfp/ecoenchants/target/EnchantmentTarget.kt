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

    @Volatile
    private var state = load(config)

    override val displayName: String
        get() = state.displayName

    override val slot: SlotType
        get() = state.slot

    override val items: List<TestableItem>
        get() = state.items

    val invalidItems: List<String>
        get() = state.invalidItems

    fun reload(config: Config) {
        state = load(config)
    }

    private fun load(config: Config): TargetState {
        val lookups = config.getStrings("items")
            .associateWith { Items.lookup(it) }

        return TargetState(
            config.getFormattedString("display-name"),
            SlotTypes[config.getString("slot")] ?: throw IllegalArgumentException(
                "Invalid slot type: ${config.getString("slot")}, options are ${
                    SlotTypes.values().map { it.id }
                }"
            ),
            lookups.values.filterNot { it is EmptyTestableItem },
            lookups.filterValues { it is EmptyTestableItem }.keys.toList()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other !is EnchantmentTarget) {
            return false
        }

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }

    private class TargetState(
        val displayName: String,
        val slot: SlotType,
        val items: List<TestableItem>,
        val invalidItems: List<String>
    )
}

internal object AllEnchantmentTarget : EnchantmentTarget {
    override val id = "all"
    override val displayName = plugin.langYml.getFormattedString("all")
    override val slot = SlotTypeAny
    @Volatile
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

package com.willfp.ecoenchants.target

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.ecoenchants.EcoEnchantsPlugin
import org.bukkit.inventory.ItemStack
import java.util.*

interface EnchantmentTarget {
    val id: String
    val displayName: String
    val slot: TargetSlot
    val items: List<TestableItem>

    fun matches(itemStack: ItemStack): Boolean {
        for (item in items) {
            if (item.matches(itemStack)) {
                return true
            }
        }
        return false
    }
}

class ConfiguredEnchantmentTarget(
    private val config: Config
) : EnchantmentTarget {
    override val id = config.getString("id")
    override val displayName = config.getFormattedString("display-name")

    override val slot = TargetSlot.valueOf(config.getString("slot").uppercase())

    override val items = config.getStrings("items")
        .map { Items.lookup(it) }
        .filterNot { it is EmptyTestableItem }

    init {
        EnchantmentTargets.addNewTarget(this)
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
}

internal object AllEnchantmentTarget : EnchantmentTarget {
    override val id = "all"
    override val displayName = EcoEnchantsPlugin.instance.langYml.getFormattedString("all")
    override val slot = TargetSlot.ANY
    override val items = EnchantmentTargets.values()
        .filterNot { it == this }
        .flatMap { it.items }

    override fun equals(other: Any?): Boolean {
        return other is AllEnchantmentTarget
    }
}

enum class TargetSlot {
    HANDS,
    ARMOR,
    ANY
}

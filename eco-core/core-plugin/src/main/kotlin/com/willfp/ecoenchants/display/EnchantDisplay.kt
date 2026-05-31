package com.willfp.ecoenchants.display

import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.display.DisplayProperties
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.formatEco
import com.willfp.ecoenchants.commands.CommandToggleDescriptions.seesEnchantmentDescriptions
import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.target.EnchantmentTargets.isEnchantable
import com.willfp.libreforge.Dispatcher
import com.willfp.libreforge.ItemProvidedHolder
import com.willfp.libreforge.ProvidedHolder
import com.willfp.libreforge.applyHolder
import com.willfp.libreforge.conditions.ConditionList
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.toPlaceholderContext
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

// Works around HIDE_POTION_EFFECTS not existing in 1.20.5+
interface HideStoredEnchantsProxy {
    fun hideStoredEnchants(fis: FastItemStack)
    fun showStoredEnchants(fis: FastItemStack)
    fun areStoredEnchantsHidden(fis: FastItemStack): Boolean
}

@Suppress("DEPRECATION")
object EnchantDisplay : DisplayModule(plugin, DisplayPriority.HIGH) {
    private val hideStateKey =
        plugin.namespacedKeyFactory.create("ecoenchantlore-skip") // Same for backwards compatibility

    private val originalHideEnchantsKey =
        plugin.namespacedKeyFactory.create("ecoenchantlore-original-hide-enchants")

    private val originalHideStoredEnchantsKey =
        plugin.namespacedKeyFactory.create("ecoenchantlore-original-hide-stored-enchants")

    private val hse = plugin.getProxy(HideStoredEnchantsProxy::class.java)

    override fun display(
        itemStack: ItemStack,
        player: Player?,
        props: DisplayProperties,
        vararg args: Any
    ) {
        val config = plugin.configYml
        val requireEnchantable = config.getBool("display.require-enchantable")

        if (!itemStack.isEnchantable && requireEnchantable) {
            return
        }

        val fast = itemStack.fast()
        val pdc = fast.persistentDataContainer
        val originallyHidEnchants = args.getOrNull(0) as? Boolean ?: fast.hasItemFlag(ItemFlag.HIDE_ENCHANTS)
        val originallyHidStoredEnchants = args.getOrNull(1) as? Boolean ?: hse.areStoredEnchantsHidden(fast)

        pdc.set(originalHideEnchantsKey, PersistentDataType.INTEGER, originallyHidEnchants.toStoredInt())
        pdc.set(originalHideStoredEnchantsKey, PersistentDataType.INTEGER, originallyHidStoredEnchants.toStoredInt())

        // Args represent hide enchants
        if (originallyHidEnchants || originallyHidStoredEnchants) {
            fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (itemStack.type == Material.ENCHANTED_BOOK) {
                hse.hideStoredEnchants(fast)
            }
            pdc.set(hideStateKey, PersistentDataType.INTEGER, 1)
            return
        } else {
            pdc.set(hideStateKey, PersistentDataType.INTEGER, 0)
        }

        // Get enchants mapped to EcoEnchantLike
        val unsorted = fast.getEnchants(true)
        if (unsorted.isEmpty()) {
            return
        }

        val lore = fast.lore
        val enchantLore = mutableListOf<String>()
        val enchants = unsorted.keys.sortForDisplay()
            .associateWith { unsorted[it]!! }

        val collapseEnabled = config.getBool("display.collapse.enabled")
        val collapseThreshold = config.getInt("display.collapse.threshold")
        val collapsePerLine = config.getInt("display.collapse.per-line")
        val collapseDelimiter = config.getFormattedString("display.collapse.delimiter")
        val descriptionsEnabled = config.getBool("display.descriptions.enabled")
        val descriptionsThreshold = config.getInt("display.descriptions.threshold")
        val enchantmentsBelowLore = config.getBool("display.enchantments-below-lore")
        val playerDispatcher = player?.toDispatcher()

        val shouldCollapse = collapseEnabled && enchants.size > collapseThreshold

        val shouldDescribe = descriptionsEnabled &&
                enchants.size <= descriptionsThreshold &&
                (player?.seesEnchantmentDescriptions ?: true)

        val formattedNames = mutableMapOf<DisplayableEnchant, String>()

        val notMetLines = mutableListOf<String>()

        for ((enchant, level) in enchants) {
            var showNotMet = false
            if (playerDispatcher != null && enchant is EcoEnchant) {
                val enchantLevel = enchant.getLevel(level)
                val holder = ItemProvidedHolder(enchantLevel, itemStack)

                val notMetDisplay = holder.getNotMetDisplay(playerDispatcher)
                notMetLines.addAll(notMetDisplay.lines.map { Display.PREFIX + it })
                showNotMet = notMetDisplay.showNameAsNotMet
            }

            val wrapped = enchant.wrap()
            formattedNames[DisplayableEnchant(wrapped, level)] =
                wrapped.getFormattedName(level, showNotMet = showNotMet)
        }

        if (shouldCollapse) {
            for (names in formattedNames.values.chunked(collapsePerLine)) {
                enchantLore.add(
                    Display.PREFIX + names.joinToString(
                        collapseDelimiter
                    )
                )
            }
        } else {
            for ((displayable, formattedName) in formattedNames) {
                val (enchant, level) = displayable

                enchantLore.add(Display.PREFIX + formattedName)

                if (shouldDescribe) {
                    enchantLore.addAll(
                        enchant.getFormattedDescription(level, player)
                        .filter { it.isNotEmpty() }.map { Display.PREFIX + it })
                }
            }
        }

        fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        if (itemStack.type == Material.ENCHANTED_BOOK) {
            hse.hideStoredEnchants(fast)
        }

        if (enchantmentsBelowLore) {
            fast.lore = lore + enchantLore + notMetLines
        } else {
            fast.lore = enchantLore + lore + notMetLines
        }
    }

    override fun revert(itemStack: ItemStack) {
        if (!itemStack.isEnchantable && plugin.configYml.getBool("display.require-enchantable")) {
            return
        }

        val fast = itemStack.fast()
        val pdc = fast.persistentDataContainer

        val originallyHidEnchants = pdc.get(originalHideEnchantsKey, PersistentDataType.INTEGER)?.toBool()
            ?: (pdc.hideState == 1)
        val originallyHidStoredEnchants = pdc.get(originalHideStoredEnchantsKey, PersistentDataType.INTEGER)?.toBool()
            ?: (pdc.hideState == 1)

        if (originallyHidEnchants) {
            fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        } else {
            fast.removeItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        if (itemStack.type == Material.ENCHANTED_BOOK) {
            if (originallyHidStoredEnchants) {
                hse.hideStoredEnchants(fast)
            } else {
                hse.showStoredEnchants(fast)
            }
        }

        pdc.remove(hideStateKey)
        pdc.remove(originalHideEnchantsKey)
        pdc.remove(originalHideStoredEnchantsKey)
    }

    override fun generateVarArgs(itemStack: ItemStack): Array<Any> {
        val fast = itemStack.fast()

        val hidesEnchants = fast.hasItemFlag(ItemFlag.HIDE_ENCHANTS)
        val hidesStoredEnchants = hse.areStoredEnchantsHidden(fast)

        return when (fast.hideState) {
            1 -> arrayOf(true, hidesStoredEnchants)
            0 -> arrayOf(false, hidesStoredEnchants)
            else -> arrayOf(
                hidesEnchants,
                hidesStoredEnchants
            )
        }
    }

    private val FastItemStack.hideState: Int
        get() = this.persistentDataContainer.hideState

    private val PersistentDataContainer.hideState: Int
        get() = this.get(hideStateKey, PersistentDataType.INTEGER) ?: -1

    private fun Boolean.toStoredInt(): Int = if (this) 1 else 0

    private fun Int.toBool(): Boolean = this == 1
}

private data class NotMetDisplay(
    val lines: List<String>,
    val showNameAsNotMet: Boolean
)

private fun ProvidedHolder.getNotMetDisplay(dispatcher: Dispatcher<*>): NotMetDisplay {
    val lines = mutableListOf<String>()
    var showNameAsNotMet = false

    fun collect(conditionList: ConditionList) {
        for (block in conditionList) {
            if (!block.showNotMet) {
                continue
            }

            if (block.isMet(dispatcher, this@getNotMetDisplay)) {
                continue
            }

            showNameAsNotMet = true

            if (block.notMetLines.isEmpty()) {
                continue
            }

            val context = block.config.applyHolder(this@getNotMetDisplay, dispatcher).toPlaceholderContext()
            lines += block.notMetLines.map {
                it.formatEco(context)
            }
        }
    }

    collect(holder.conditions)
    for (effect in holder.effects) {
        collect(effect.conditions)
    }

    return NotMetDisplay(lines, showNameAsNotMet)
}

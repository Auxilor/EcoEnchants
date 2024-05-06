package com.willfp.ecoenchants.display

import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.display.DisplayProperties
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.items.builder.modify
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.commands.CommandToggleDescriptions.Companion.seesEnchantmentDescriptions
import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.target.EnchantmentTargets.isEnchantable
import com.willfp.libreforge.ItemProvidedHolder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

// Works around HIDE_POTION_EFFECTS not existing in 1.20.5+
interface HideStoredEnchantsProxy {
    fun hideStoredEnchants(fis: FastItemStack)
    fun showStoredEnchants(fis: FastItemStack)
    fun areStoredEnchantsHidden(fis: FastItemStack): Boolean
}

@Suppress("DEPRECATION")
class EnchantDisplay(private val plugin: EcoEnchantsPlugin) : DisplayModule(plugin, DisplayPriority.HIGH) {
    private val hideStateKey =
        plugin.namespacedKeyFactory.create("ecoenchantlore-skip") // Same for backwards compatibility

    private val hse = plugin.getProxy(HideStoredEnchantsProxy::class.java)

    override fun display(
        itemStack: ItemStack,
        player: Player?,
        props: DisplayProperties,
        vararg args: Any
    ) {
        if (!itemStack.isEnchantable && plugin.configYml.getBool("display.require-enchantable")) {
            return
        }

        val fast = itemStack.fast()
        val pdc = fast.persistentDataContainer

        // Args represent hide enchants
        if (args[0] == true) {
            fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (itemStack.type == Material.ENCHANTED_BOOK) {
                hse.hideStoredEnchants(fast)
            }
            pdc.set(hideStateKey, PersistentDataType.INTEGER, 1)
            return
        } else {
            pdc.set(hideStateKey, PersistentDataType.INTEGER, 0)
        }

        val lore = fast.lore
        val enchantLore = mutableListOf<String>()

        // Get enchants mapped to EcoEnchantLike
        val unsorted = fast.getEnchants(true)
        val enchants = unsorted.keys.sortForDisplay()
            .associateWith { unsorted[it]!! }

        val shouldCollapse = plugin.configYml.getBool("display.collapse.enabled") &&
                enchants.size > plugin.configYml.getInt("display.collapse.threshold")

        val shouldDescribe = (plugin.configYml.getBool("display.descriptions.enabled") &&
                enchants.size <= plugin.configYml.getInt("display.descriptions.threshold")
                && player?.seesEnchantmentDescriptions ?: true)

        val formattedNames = mutableMapOf<DisplayableEnchant, String>()

        val notMetLines = mutableListOf<String>()

        for ((enchant, level) in enchants) {
            var showNotMet = false
            if (player != null && enchant is EcoEnchant) {
                val enchantLevel = enchant.getLevel(level)
                val holder = ItemProvidedHolder(enchantLevel, itemStack)

                val enchantNotMetLines = holder.getNotMetLines(player).map { Display.PREFIX + it }
                notMetLines.addAll(enchantNotMetLines)

                if (enchantNotMetLines.isNotEmpty() || holder.isShowingAnyNotMet(player)) {
                    showNotMet = true
                }
            }

            formattedNames[DisplayableEnchant(enchant.wrap(), level)] =
                enchant.wrap().getFormattedName(level, showNotMet = showNotMet)
        }

        if (shouldCollapse) {
            val perLine = plugin.configYml.getInt("display.collapse.per-line")
            for (names in formattedNames.values.chunked(perLine)) {
                enchantLore.add(
                    Display.PREFIX + names.joinToString(
                        plugin.configYml.getFormattedString("display.collapse.delimiter")
                    )
                )
            }
        } else {
            for ((displayable, formattedName) in formattedNames) {
                val (enchant, level) = displayable

                enchantLore.add(Display.PREFIX + formattedName)

                if (shouldDescribe) {
                    enchantLore.addAll(enchant.getFormattedDescription(level, player)
                        .filter { it.isNotEmpty() }.map { Display.PREFIX + it })
                }
            }
        }

        fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        if (itemStack.type == Material.ENCHANTED_BOOK) {
            hse.hideStoredEnchants(fast)
        }

        fast.lore = enchantLore + lore + notMetLines
    }

    override fun revert(itemStack: ItemStack) {
        if (!itemStack.isEnchantable && plugin.configYml.getBool("display.require-enchantable")) {
            return
        }

        val fast = itemStack.fast()
        val pdc = fast.persistentDataContainer

        if (pdc.hideState != 1) {
            fast.removeItemFlags(ItemFlag.HIDE_ENCHANTS)

            if (itemStack.type == Material.ENCHANTED_BOOK) {
                hse.showStoredEnchants(fast)
            }
        }

        pdc.remove(hideStateKey)
    }

    override fun generateVarArgs(itemStack: ItemStack): Array<Any> {
        val fast = itemStack.fast()

        return when (fast.hideState) {
            1 -> arrayOf(true)
            0 -> arrayOf(false)
            else -> arrayOf(
                fast.hasItemFlag(ItemFlag.HIDE_ENCHANTS)
                        || hse.areStoredEnchantsHidden(fast)
            )
        }
    }

    private val FastItemStack.hideState: Int
        get() = this.persistentDataContainer.hideState

    private val PersistentDataContainer.hideState: Int
        get() = this.get(hideStateKey, PersistentDataType.INTEGER) ?: -1
}

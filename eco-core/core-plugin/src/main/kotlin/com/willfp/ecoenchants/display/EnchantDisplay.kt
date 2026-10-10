package com.willfp.ecoenchants.display

import com.willfp.eco.core.display.DisplayContext
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.toComponent
import com.willfp.ecoenchants.commands.CommandToggleDescriptions.seesEnchantmentDescriptions
import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.dragdrop.dragAndDropPriceDisplay
import com.willfp.ecoenchants.dragdrop.isDragAndDropEnabled
import com.willfp.ecoenchants.enchant.ecoEnchant
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.target.EnchantmentTargets.isEnchantable
import com.willfp.libreforge.ItemProvidedHolder
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

@Suppress("DEPRECATION")
object EnchantDisplay : DisplayModule(plugin, DisplayPriority.HIGH) {
    private val hideStateKey =
        plugin.namespacedKeyFactory.create("ecoenchantlore-skip") // Same for backwards compatibility

    override fun display(context: DisplayContext) {
        val itemStack = context.itemStack
        val player = context.player

        if (!itemStack.isEnchantable && plugin.configYml.getBool("display.require-enchantable")) {
            return
        }

        val fast = itemStack.fast()
        val pdc = fast.persistentDataContainer

        // Args represent hide enchants
        if (context.varArgs[0] == true) {
            fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (itemStack.type == Material.ENCHANTED_BOOK) {
                fast.hideStoredEnchants()
            }
            pdc.set(hideStateKey, PersistentDataType.INTEGER, 1)
            return
        } else {
            pdc.set(hideStateKey, PersistentDataType.INTEGER, 0)
        }

        val enchantLore = mutableListOf<Component>()

        // Get enchants mapped to EcoEnchantLike
        val unsorted = fast.getEnchants(true)
        val enchants = unsorted.keys.sortForDisplay()
            .associateWith { unsorted[it]!! }

        val shouldCollapse = plugin.configYml.getBool("display.collapse.enabled") &&
                enchants.size > plugin.configYml.getInt("display.collapse.threshold")

        val shouldDescribe = (plugin.configYml.getBool("display.descriptions.enabled") &&
                enchants.size <= plugin.configYml.getInt("display.descriptions.threshold")
                && player?.seesEnchantmentDescriptions ?: true)

        val shouldShowTargets = itemStack.type == Material.ENCHANTED_BOOK &&
                plugin.configYml.getBool("display.book-targets.enabled")

        val shouldShowDragAndDropPrice = itemStack.type == Material.ENCHANTED_BOOK &&
                plugin.configYml.getBool("display.book-drag-and-drop-price.enabled")

        val formattedNames = mutableMapOf<DisplayableEnchant, String>()

        val notMetLines = mutableListOf<Component>()

        for ((enchant, level) in enchants) {
            var showNotMet = false
            val ecoEnchant = enchant.ecoEnchant
            if (player != null && ecoEnchant != null) {
                val enchantLevel = ecoEnchant.getLevel(level)
                val holder = ItemProvidedHolder(enchantLevel, itemStack)

                val enchantNotMetLines = holder.getNotMetLineComponents(player)
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
                    names.joinToString(
                        plugin.configYml.getFormattedString("display.collapse.delimiter")
                    ).toComponent()
                )
            }
        } else {
            for ((displayable, formattedName) in formattedNames) {
                val (enchant, level) = displayable

                enchantLore.add(formattedName.toComponent())

                if (shouldDescribe) {
                    enchantLore.addAll(
                        enchant.getFormattedDescription(level, player)
                            .filter { it.isNotEmpty() }
                            .map { it.toComponent() }
                    )
                }

                if (shouldShowTargets && enchant.targets.isNotEmpty()) {
                    enchantLore.add(
                        plugin.configYml.getFormattedString("display.book-targets.format")
                            .replace("%targets%", enchant.targets.joinToString(", ") { it.displayName })
                            .toComponent()
                    )
                }

                if (shouldShowDragAndDropPrice && player != null && enchant.isDragAndDropEnabled()) {
                    enchantLore.add(
                        plugin.configYml.getFormattedString("display.book-drag-and-drop-price.format")
                            .replace("%price%", enchant.dragAndDropPriceDisplay(player, level))
                            .toComponent()
                    )
                }
            }
        }

        fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        if (itemStack.type == Material.ENCHANTED_BOOK) {
            fast.hideStoredEnchants()
        }

        if (plugin.configYml.getBool("display.enchantments-below-lore")) {
            context.lore.append(enchantLore + notMetLines)
        } else {
            context.lore.prepend(enchantLore)
            context.lore.append(notMetLines)
        }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun revert(itemStack: ItemStack) {
        if (!itemStack.isEnchantable && plugin.configYml.getBool("display.require-enchantable")) {
            return
        }

        val fast = itemStack.fast()
        val pdc = fast.persistentDataContainer

        if (pdc.hideState != 1) {
            fast.removeItemFlags(ItemFlag.HIDE_ENCHANTS)

            if (itemStack.type == Material.ENCHANTED_BOOK) {
                fast.showStoredEnchants()
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
                        || fast.areStoredEnchantsHidden
            )
        }
    }

    private val FastItemStack.hideState: Int
        get() = this.persistentDataContainer.hideState

    private val PersistentDataContainer.hideState: Int
        get() = this.get(hideStateKey, PersistentDataType.INTEGER) ?: -1
}

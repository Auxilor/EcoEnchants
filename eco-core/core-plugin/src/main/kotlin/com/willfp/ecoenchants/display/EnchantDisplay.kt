package com.willfp.ecoenchants.display

import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.display.DisplayProperties
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.commands.CommandToggleDescriptions.Companion.seesEnchantmentDescriptions
import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.enchants.wrap
import com.willfp.ecoenchants.target.EnchantmentTargets.isEnchantable
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

@Suppress("DEPRECATION")
class EnchantDisplay(private val plugin: EcoEnchantsPlugin) : DisplayModule(plugin, DisplayPriority.HIGH) {
    private val internalHideEnchants =
        plugin.namespacedKeyFactory.create("ecoenchantlore-skip") // Same for backwards compatibility

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

        // Args represent hide enchants - adding extra check ported from 8.x.x
        if (args[0] == true || pdc.has(internalHideEnchants, PersistentDataType.INTEGER)) {
            fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (itemStack.type == Material.ENCHANTED_BOOK) {
                fast.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
            }
            pdc.set(internalHideEnchants, PersistentDataType.INTEGER, 1)
            return
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
                || pdc.has(plugin.namespacedKeyFactory.create("force-describe"), PersistentDataType.INTEGER)

        val formattedNames = mutableMapOf<DisplayableEnchant, String>()

        val notMetLines = mutableListOf<String>()

        for ((enchant, level) in enchants) {
            if (player != null && enchant is EcoEnchant) {
                notMetLines.addAll(enchant.getLevel(level).getNotMetLines(player).map { Display.PREFIX + it })
            }

            formattedNames[DisplayableEnchant(enchant.wrap(), level)] = enchant.wrap().getFormattedName(level)
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
                    enchantLore.addAll(enchant.getFormattedDescription(level).map { Display.PREFIX + it })
                }
            }
        }

        fast.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        if (itemStack.type == Material.ENCHANTED_BOOK) {
            fast.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
        }

        fast.lore = enchantLore + lore + notMetLines
    }

    override fun revert(itemStack: ItemStack) {
        if (!itemStack.isEnchantable && plugin.configYml.getBool("display.require-enchantable")) {
            return
        }

        val fast = itemStack.fast()
        val pdc = fast.persistentDataContainer

        if (!pdc.has(internalHideEnchants, PersistentDataType.INTEGER)) {
            fast.removeItemFlags(ItemFlag.HIDE_ENCHANTS)

            if (itemStack.type == Material.ENCHANTED_BOOK) {
                fast.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
            }
        }

        pdc.remove(internalHideEnchants)
    }

    override fun generateVarArgs(itemStack: ItemStack): Array<Any> {
        val fast = itemStack.fast()

        // I'm not including the internal hide enchants check here because... I don't know why.
        // I'm copying over from 8.x.x because I know it worked then.
        return arrayOf(
            fast.hasItemFlag(ItemFlag.HIDE_ENCHANTS)
                    || fast.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
        )
    }
}

package com.willfp.ecoenchants.display

import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.persistence.PersistentDataType
import wiki.ecohub.config.Config
import wiki.ecohub.core.plugin.startup.Prerequisite
import wiki.ecohub.game.item.Items
import wiki.ecohub.game.item.copyModelDataFrom
import wiki.ecohub.game.item.hasAnyModelData
import wiki.ecohub.game.item.lookup.EmptyTestableItem
import wiki.ecohub.game.item.lookup.TestableItem
import wiki.ecohub.game.item.removeModelData

internal fun Config.getBookItem(): ItemStack? =
    this.getStringOrNull("book-item")
        ?.let { Items.lookup(it) }
        ?.takeUnless { it is EmptyTestableItem }
        ?.let(TestableItem::getItem)

object BookItemDisplay {
    private val appliedKey = plugin.namespacedKeyFactory.create("book-item-applied")

    /**
     * Item models and tooltip styles were added in 1.21.2.
     */
    private val hasItemComponents: Boolean
        get() = Prerequisite.hasVersion(1, 21, 2).isMet

    fun apply(itemStack: ItemStack) {
        if (itemStack.type != Material.ENCHANTED_BOOK) {
            return
        }

        val meta = itemStack.itemMeta as? EnchantmentStorageMeta ?: return

        if (hasItemComponents && (meta.hasItemModel() || meta.hasTooltipStyle())
            || meta.hasAnyModelData() || meta.hasEnchantmentGlintOverride()
        ) {
            return
        }

        val enchant = meta.storedEnchants.keys.sortForDisplay().firstOrNull()?.wrap() ?: return
        val bookMeta = if (plugin.configYml.getString("display.book-item.priority").equals("rarity", ignoreCase = true)) {
            enchant.enchantmentRarity.bookItem ?: enchant.type.bookItem
        } else {
            enchant.type.bookItem ?: enchant.enchantmentRarity.bookItem
        }?.itemMeta ?: return

        if (hasItemComponents) {
            if (bookMeta.hasItemModel()) {
                meta.itemModel = bookMeta.itemModel
            }

            if (bookMeta.hasTooltipStyle()) {
                meta.tooltipStyle = bookMeta.tooltipStyle
            }
        }

        meta.copyModelDataFrom(bookMeta)

        if (bookMeta.hasEnchantmentGlintOverride()) {
            meta.setEnchantmentGlintOverride(bookMeta.enchantmentGlintOverride)
        }

        meta.persistentDataContainer.set(appliedKey, PersistentDataType.BOOLEAN, true)
        itemStack.itemMeta = meta
    }

    fun revert(itemStack: ItemStack) {
        if (itemStack.type != Material.ENCHANTED_BOOK) {
            return
        }

        val meta = itemStack.itemMeta ?: return

        if (!meta.persistentDataContainer.has(appliedKey)) {
            return
        }

        if (hasItemComponents) {
            meta.itemModel = null
            meta.tooltipStyle = null
        }

        meta.removeModelData()
        meta.setEnchantmentGlintOverride(null)
        meta.persistentDataContainer.remove(appliedKey)
        itemStack.itemMeta = meta
    }
}

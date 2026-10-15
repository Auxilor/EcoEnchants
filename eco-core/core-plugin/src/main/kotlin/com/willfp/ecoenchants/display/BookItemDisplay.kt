package com.willfp.ecoenchants.display

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.persistence.PersistentDataType

internal fun Config.getBookItem(): ItemStack? =
    this.getStringOrNull("book-item")
        ?.let { Items.lookup(it) }
        ?.takeUnless { it is EmptyTestableItem }
        ?.let(TestableItem::getItem)

object BookItemDisplay {
    private val appliedKey = plugin.namespacedKeyFactory.create("book-item-applied")

    fun apply(itemStack: ItemStack) {
        if (itemStack.type != Material.ENCHANTED_BOOK) {
            return
        }

        val meta = itemStack.itemMeta as? EnchantmentStorageMeta ?: return

        if (meta.hasItemModel() || meta.hasCustomModelDataComponent()
            || meta.hasEnchantmentGlintOverride() || meta.hasTooltipStyle()
        ) {
            return
        }

        val enchant = meta.storedEnchants.keys.sortForDisplay().firstOrNull()?.wrap() ?: return
        val bookMeta = if (plugin.configYml.getString("display.book-item.priority").equals("rarity", ignoreCase = true)) {
            enchant.enchantmentRarity.bookItem ?: enchant.type.bookItem
        } else {
            enchant.type.bookItem ?: enchant.enchantmentRarity.bookItem
        }?.itemMeta ?: return

        if (bookMeta.hasItemModel()) {
            meta.itemModel = bookMeta.itemModel
        }

        if (bookMeta.hasCustomModelDataComponent()) {
            meta.setCustomModelDataComponent(bookMeta.customModelDataComponent)
        }

        if (bookMeta.hasEnchantmentGlintOverride()) {
            meta.setEnchantmentGlintOverride(bookMeta.enchantmentGlintOverride)
        }

        if (bookMeta.hasTooltipStyle()) {
            meta.tooltipStyle = bookMeta.tooltipStyle
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

        meta.itemModel = null
        meta.setCustomModelDataComponent(null)
        meta.setEnchantmentGlintOverride(null)
        meta.tooltipStyle = null
        meta.persistentDataContainer.remove(appliedKey)
        itemStack.itemMeta = meta
    }
}

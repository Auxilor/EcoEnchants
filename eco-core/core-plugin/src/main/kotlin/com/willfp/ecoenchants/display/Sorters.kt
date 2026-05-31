package com.willfp.ecoenchants.display

import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.enchantments.Enchantment

object EnchantSorter {
    private var sortByRarity = false
    private var sortByType = false
    private var sortByLength = false
    private var comparator: Comparator<DisplaySortEntry> = compareBy<DisplaySortEntry> { it.name }

    internal fun reload() {
        sortByRarity = plugin.configYml.getBool("display.sort.rarity")
        sortByType = plugin.configYml.getBool("display.sort.type")
        sortByLength = plugin.configYml.getBool("display.sort.length")

        val comparators = mutableListOf<Comparator<DisplaySortEntry>>()

        if (sortByRarity) {
            comparators += compareBy<DisplaySortEntry> { it.rarityOrder }
        }

        if (sortByType) {
            comparators += compareBy<DisplaySortEntry> { it.typeOrder }
        }

        if (sortByLength) {
            comparators += compareBy<DisplaySortEntry> { it.nameLength }
        } else {
            comparators += compareBy<DisplaySortEntry> { it.name }
        }

        comparator = comparators.reduce { current, next -> current.then(next) }
    }

    fun Collection<Enchantment>.sortForDisplay(): List<Enchantment> =
        this.mapNotNull { it.toDisplaySortEntry() }
            .sortedWith(comparator)
            .map { it.enchantment }

    private fun Enchantment.toDisplaySortEntry(): DisplaySortEntry? {
        val wrapped = this.wrap()
        val rarityOrder = if (sortByRarity) {
            RaritySorter.orderOf(wrapped.enchantmentRarity) ?: return null
        } else {
            0
        }
        val typeOrder = if (sortByType) {
            TypeSorter.orderOf(wrapped.type) ?: return null
        } else {
            0
        }

        @Suppress("DEPRECATION")
        val name = org.bukkit.ChatColor.stripColor(wrapped.getFormattedName(0)) ?: ""

        return DisplaySortEntry(
            enchantment = this,
            rarityOrder = rarityOrder,
            typeOrder = typeOrder,
            nameLength = name.length,
            name = name
        )
    }
}

private data class DisplaySortEntry(
    val enchantment: Enchantment,
    val rarityOrder: Int,
    val typeOrder: Int,
    val nameLength: Int,
    val name: String
)

object TypeSorter {
    private var typeOrder = emptyMap<EnchantmentType, Int>()

    fun update() {
        typeOrder = plugin.configYml.getStrings("display.sort.type-order")
            .mapIndexedNotNull { index, id ->
                EnchantmentTypes[id]?.let { it to index }
            }
            .toMap()
    }

    internal fun orderOf(type: EnchantmentType): Int? = typeOrder[type]
}

object RaritySorter {
    private var rarityOrder = emptyMap<EnchantmentRarity, Int>()

    fun update() {
        rarityOrder = plugin.configYml.getStrings("display.sort.rarity-order")
            .mapIndexedNotNull { index, id ->
                EnchantmentRarities[id]?.let { it to index }
            }
            .toMap()
    }

    internal fun orderOf(rarity: EnchantmentRarity): Int? = rarityOrder[rarity]
}

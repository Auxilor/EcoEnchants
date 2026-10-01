package com.willfp.ecoenchants.display

import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.enchantments.Enchantment

interface EnchantmentSorter {
    fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment>
}

object EnchantSorter {
    // Replaced rather than mutated: display runs on many threads at once.
    @Volatile
    private var sorters = emptyList<EnchantmentSorter>()

    internal fun reload() {
        val sorters = mutableListOf<EnchantmentSorter>()

        if (plugin.configYml.getBool("display.sort.rarity")) {
            sorters.add(RaritySorter)
        }

        if (plugin.configYml.getBool("display.sort.type")) {
            sorters.add(TypeSorter)
        }

        if (plugin.configYml.getBool("display.sort.length")) {
            sorters.add(LengthSorter)
        }

        this.sorters = sorters
    }

    fun Collection<Enchantment>.sortForDisplay(): List<Enchantment> {
        val sorters = sorters
        return sorters.getSafely(0).sort(this, sorters.drop(1))
    }
}

fun List<EnchantmentSorter>.getSafely(index: Int) =
    this.getOrNull(index) ?: AlphabeticSorter

object AlphabeticSorter : EnchantmentSorter {
    override fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment> {
        @Suppress("DEPRECATION")
        return enchantments.sortedBy { org.bukkit.ChatColor.stripColor(it.wrap().getFormattedName(0)) }
    }
}

object LengthSorter : EnchantmentSorter {
    override fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment> {
        @Suppress("DEPRECATION")
        return enchantments.sortedBy { org.bukkit.ChatColor.stripColor(it.wrap().getFormattedName(0))?.length ?: 0 }
    }
}

object TypeSorter : EnchantmentSorter {
    @Volatile
    private var types = emptyList<EnchantmentType>()

    fun update() {
        types = plugin.configYml.getStrings("display.sort.type-order").mapNotNull {
            EnchantmentTypes[it]
        }
    }

    override fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment> {
        val sorted = children.getSafely(0).sort(enchantments, children.drop(1))
        val enchants = mutableListOf<Enchantment>()
        for (type in types) {
            enchants.addAll(sorted.filter { it.wrap().type == type })
        }
        return enchants
    }
}

object RaritySorter : EnchantmentSorter {
    @Volatile
    private var rarities = emptyList<EnchantmentRarity>()

    fun update() {
        rarities = plugin.configYml.getStrings("display.sort.rarity-order").mapNotNull {
            EnchantmentRarities[it]
        }
    }

    override fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment> {
        val sorted = children.getSafely(0).sort(enchantments, children.drop(1))
        val enchants = mutableListOf<Enchantment>()
        for (rarity in rarities) {
            enchants.addAll(sorted.filter { it.wrap().enchantmentRarity == rarity })
        }
        return enchants
    }
}

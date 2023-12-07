package com.willfp.ecoenchants.display

import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment

interface EnchantmentSorter {
    fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment>
}

object EnchantSorter {
    private val sorters = mutableListOf<EnchantmentSorter>()

    internal fun reload(plugin: EcoEnchantsPlugin) {
        sorters.clear()

        if (plugin.configYml.getBool("display.sort.rarity")) {
            sorters.add(RaritySorter)
        }

        if (plugin.configYml.getBool("display.sort.type")) {
            sorters.add(TypeSorter)
        }

        if (plugin.configYml.getBool("display.sort.length")) {
            sorters.add(LengthSorter)
        }
    }

    fun Collection<Enchantment>.sortForDisplay(): List<Enchantment> =
        sorters.getSafely(0).sort(this, sorters.drop(1))
}

fun List<EnchantmentSorter>.getSafely(index: Int) =
    this.getOrNull(index) ?: AlphabeticSorter

object AlphabeticSorter : EnchantmentSorter {
    override fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment> {
        return enchantments.sortedBy { ChatColor.stripColor(it.wrap().getFormattedName(0)) }
    }
}

object LengthSorter : EnchantmentSorter {
    override fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment> {
        return enchantments.sortedBy { ChatColor.stripColor(it.wrap().getFormattedName(0))!!.length }
    }
}

object TypeSorter : EnchantmentSorter {
    private val types = mutableListOf<EnchantmentType>()

    fun update(plugin: EcoEnchantsPlugin) {
        types.clear()
        types.addAll(plugin.configYml.getStrings("display.sort.type-order").mapNotNull {
            EnchantmentTypes[it]
        })
    }

    override fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment> {
        val enchants = mutableListOf<Enchantment>()

        for (type in types) {
            for (enchantment in children.getSafely(0).sort(enchantments, children.drop(1))) {
                if (type != enchantment.wrap().type) {
                    continue
                }

                enchants.add(enchantment)
            }
        }

        return enchants
    }
}

object RaritySorter : EnchantmentSorter {
    private val rarities = mutableListOf<EnchantmentRarity>()

    fun update(plugin: EcoEnchantsPlugin) {
        rarities.clear()
        rarities.addAll(plugin.configYml.getStrings("display.sort.rarity-order").mapNotNull {
            EnchantmentRarities[it]
        })
    }

    override fun sort(enchantments: Collection<Enchantment>, children: List<EnchantmentSorter>): List<Enchantment> {
        val enchants = mutableListOf<Enchantment>()

        for (rarity in rarities) {
            for (enchantment in children.getSafely(0).sort(enchantments, children.drop(1))) {
                if (rarity != enchantment.wrap().enchantmentRarity) {
                    continue
                }

                enchants.add(enchantment)
            }
        }

        return enchants
    }
}

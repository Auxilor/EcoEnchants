package com.willfp.ecoenchants.proxy.v26_1_1.registration

import com.willfp.ecoenchants.display.getFormattedNameComponent
import com.willfp.ecoenchants.enchant.EcoEnchant
import io.papermc.paper.adventure.PaperAdventure
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentMap
import net.minecraft.network.chat.Component
import net.minecraft.world.item.enchantment.Enchantment

fun vanillaEcoEnchantsEnchantment(enchant: EcoEnchant): Enchantment {
    val definition = Enchantment.definition(
        HolderSet.empty(),
        1, // 1.21 hardcodes a minimum weight of 1
        enchant.maximumLevel,
        Enchantment.constantCost(1),
        Enchantment.constantCost(1),
        0
    )

    val description = enchant.nameTranslationKey?.let { Component.translatable(it) }
        ?: PaperAdventure.asVanilla(enchant.getFormattedNameComponent(0))

    return Enchantment(description, definition, HolderSet.empty(), DataComponentMap.EMPTY)
}

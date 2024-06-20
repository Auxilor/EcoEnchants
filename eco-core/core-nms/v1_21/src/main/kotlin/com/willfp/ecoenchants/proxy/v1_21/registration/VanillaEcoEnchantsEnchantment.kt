package com.willfp.ecoenchants.proxy.v1_21.registration

import com.willfp.ecoenchants.enchant.EcoEnchant
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment

fun vanillaEcoEnchantsEnchantment(enchant: EcoEnchant): Enchantment {
    val enchantment = Enchantment.enchantment(
        Enchantment.definition(
            HolderSet.empty(),
            0, // Weight is 0, so we can use our own rarity system
            enchant.maximumLevel,
            Enchantment.constantCost(1),
            Enchantment.constantCost(1),
            0
        )
    )

    return enchantment.build(ResourceLocation.withDefaultNamespace(enchant.id))
}

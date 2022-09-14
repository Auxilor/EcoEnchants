package com.willfp.ecoenchants.proxy.proxies

import com.willfp.ecoenchants.vanilla.VanillaEnchantmentData
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

interface EcoCraftEnchantmentManagerProxy {
    fun registerNewCraftEnchantment(enchantment: Enchantment, data: VanillaEnchantmentData)
}

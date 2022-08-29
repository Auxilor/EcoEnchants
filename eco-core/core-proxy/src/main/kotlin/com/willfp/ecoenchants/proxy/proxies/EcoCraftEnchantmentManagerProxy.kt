package com.willfp.ecoenchants.proxy.proxies

import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

interface EcoCraftEnchantmentManagerProxy {
    fun registerNewCraftEnchantment(enchantment: Enchantment, maxLevel: Int, conflicts: Collection<NamespacedKey>)
}

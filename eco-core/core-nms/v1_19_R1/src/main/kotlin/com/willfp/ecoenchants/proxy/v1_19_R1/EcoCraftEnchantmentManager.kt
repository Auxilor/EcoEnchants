package com.willfp.ecoenchants.proxy.v1_19_R1

import com.willfp.ecoenchants.enchant.EcoCraftEnchantmentManagerProxy
import com.willfp.ecoenchants.enchant.VanillaEnchantmentData
import org.bukkit.enchantments.Enchantment

class EcoCraftEnchantmentManager : EcoCraftEnchantmentManagerProxy {
    override fun registerNewCraftEnchantment(
        enchantment: Enchantment,
        data: VanillaEnchantmentData
    ) {
        for (enchant in net.minecraft.core.IRegistry.W) {
            val key = org.bukkit.craftbukkit.v1_19_R1.util.CraftNamespacedKey.fromMinecraft(
                net.minecraft.core.IRegistry.W.b(enchant)
            )
            if (key.key != enchantment.key.key) {
                continue
            }
            EcoCraftEnchantment(enchant, data).register()
        }
    }
}

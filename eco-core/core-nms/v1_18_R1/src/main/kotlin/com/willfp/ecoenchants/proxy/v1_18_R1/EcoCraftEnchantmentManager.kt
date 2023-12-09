package com.willfp.ecoenchants.proxy.v1_18_R1

import com.willfp.ecoenchants.enchant.EcoCraftEnchantmentManagerProxy
import com.willfp.ecoenchants.enchant.VanillaEnchantmentData
import com.willfp.ecoenchants.enchant.registration.legacy.LegacyEnchantmentRegisterer
import org.bukkit.enchantments.Enchantment

class EcoCraftEnchantmentManager : EcoCraftEnchantmentManagerProxy {
    override fun registerNewCraftEnchantment(
        enchantment: Enchantment,
        data: VanillaEnchantmentData
    ) {
        for (enchant in net.minecraft.core.IRegistry.Y) {
            val key = org.bukkit.craftbukkit.v1_18_R1.util.CraftNamespacedKey.fromMinecraft(
                net.minecraft.core.IRegistry.Y.b(enchant)
            )
            if (key.key != enchantment.key.key) {
                continue
            }

            LegacyEnchantmentRegisterer.registerToBukkit(EcoCraftEnchantment(enchant, data))
        }
    }
}

package com.willfp.ecoenchants.proxy.v1_19_R2

import com.willfp.ecoenchants.enchant.EcoCraftEnchantmentManagerProxy
import com.willfp.ecoenchants.enchant.VanillaEnchantmentData
import com.willfp.ecoenchants.enchant.registration.legacy.LegacyEnchantmentRegisterer
import net.minecraft.core.registries.BuiltInRegistries
import org.bukkit.enchantments.Enchantment

class EcoCraftEnchantmentManager : EcoCraftEnchantmentManagerProxy {
    override fun registerNewCraftEnchantment(
        enchantment: Enchantment,
        data: VanillaEnchantmentData
    ) {
        for (enchant in BuiltInRegistries.g) {
            val key = org.bukkit.craftbukkit.v1_19_R2.util.CraftNamespacedKey.fromMinecraft(
                BuiltInRegistries.g.b(enchant)
            )
            if (key.key != enchantment.key.key) {
                continue
            }

            LegacyEnchantmentRegisterer.registerToBukkit(EcoCraftEnchantment(enchant, data))
        }
    }
}

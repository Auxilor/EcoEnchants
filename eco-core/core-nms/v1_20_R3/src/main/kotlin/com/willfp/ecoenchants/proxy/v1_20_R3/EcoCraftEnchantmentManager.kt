package com.willfp.ecoenchants.proxy.v1_20_R3

import com.willfp.ecoenchants.proxy.proxies.EcoCraftEnchantmentManagerProxy
import com.willfp.ecoenchants.proxy.v1_20_R3.EcoCraftEnchantment
import com.willfp.ecoenchants.vanilla.VanillaEnchantmentData
import net.minecraft.core.registries.BuiltInRegistries
import org.bukkit.enchantments.Enchantment

class EcoCraftEnchantmentManager : EcoCraftEnchantmentManagerProxy {
    override fun registerNewCraftEnchantment(
        enchantment: Enchantment,
        data: VanillaEnchantmentData
    ) {
        for (enchant in BuiltInRegistries.ENCHANTMENT) {
            val key = org.bukkit.craftbukkit.v1_20_R3.util.CraftNamespacedKey.fromMinecraft(
                BuiltInRegistries.ENCHANTMENT.getKey(enchant)
            )

            if (key.key != enchantment.key.key) {
                continue
            }

            EcoCraftEnchantment(enchant, key, data).register()
        }
    }
}

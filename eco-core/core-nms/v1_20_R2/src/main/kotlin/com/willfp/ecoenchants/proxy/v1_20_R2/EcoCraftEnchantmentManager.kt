package com.willfp.ecoenchants.proxy.v1_20_R2

import com.willfp.ecoenchants.proxy.proxies.EcoCraftEnchantmentManagerProxy
import com.willfp.ecoenchants.vanilla.VanillaEnchantmentData
import net.minecraft.core.registries.BuiltInRegistries
import org.bukkit.enchantments.Enchantment

class EcoCraftEnchantmentManager : EcoCraftEnchantmentManagerProxy {
    override fun registerNewCraftEnchantment(
        enchantment: Enchantment,
        data: VanillaEnchantmentData
    ) {
        for (enchant in BuiltInRegistries.g) {
            val key = org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey.fromMinecraft(
                BuiltInRegistries.g.b(enchant)
            )
            if (key.key != enchantment.key.key) {
                continue
            }
            EcoCraftEnchantment(enchant, data).register()
        }
    }
}

package com.willfp.ecoenchants.proxy.v1_20_6

import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.impl.EcoEnchantBase
import com.willfp.ecoenchants.enchant.registration.modern.ModernEnchantmentRegistererProxy
import com.willfp.ecoenchants.proxy.v1_20_6.registration.EcoEnchantsCraftEnchantment
import com.willfp.ecoenchants.proxy.v1_20_6.registration.ModifiedVanillaCraftEnchantment
import com.willfp.ecoenchants.proxy.v1_20_6.registration.VanillaEcoEnchantsEnchantment
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.CraftRegistry
import org.bukkit.craftbukkit.util.CraftNamespacedKey
import org.bukkit.enchantments.Enchantment
import java.util.IdentityHashMap
import java.util.function.BiFunction

class ModernEnchantmentRegisterer : ModernEnchantmentRegistererProxy {
    private val frozenField = MappedRegistry::class.java
        .declaredFields
        .filter { it.type.isPrimitive }[0]
        .apply { isAccessible = true }

    private val unregisteredIntrusiveHoldersField = MappedRegistry::class.java
        .declaredFields
        .last { it.type == Map::class.java }
        .apply { isAccessible = true }

    private val minecraftToBukkit = CraftRegistry::class.java
        .getDeclaredField("minecraftToBukkit")
        .apply { isAccessible = true }

    private val vanillaEnchantments = Enchantments::class.java
        .declaredFields
        .filter { it.type == net.minecraft.world.item.enchantment.Enchantment::class.java }
        .map { it.get(null) as net.minecraft.world.item.enchantment.Enchantment }
        .mapNotNull { BuiltInRegistries.ENCHANTMENT.getKey(it) }
        .map { CraftNamespacedKey.fromMinecraft(it) }
        .toSet()

    override fun replaceRegistry() {
        val newRegistryMTB =
            BiFunction<NamespacedKey, net.minecraft.world.item.enchantment.Enchantment, Enchantment> { key, registry ->
                val isVanilla = vanillaEnchantments.contains(key)
                val eco = EcoEnchants.getByID(key.key)

                if (isVanilla) {
                    ModifiedVanillaCraftEnchantment(key, registry)
                } else if (eco != null) {
                    eco as Enchantment
                } else {
                    null
                }
            }

        // Update bukkit registry
        minecraftToBukkit.set(org.bukkit.Registry.ENCHANTMENT, newRegistryMTB)

        // Unfreeze NMS registry
        frozenField.set(BuiltInRegistries.ENCHANTMENT, false)
        unregisteredIntrusiveHoldersField.set(
            BuiltInRegistries.ENCHANTMENT,
            IdentityHashMap<net.minecraft.world.item.enchantment.Enchantment,
                    Holder.Reference<net.minecraft.world.item.enchantment.Enchantment>>()
        )
    }

    override fun register(enchant: EcoEnchantBase): Enchantment {
        if (BuiltInRegistries.ENCHANTMENT.containsKey(CraftNamespacedKey.toMinecraft(enchant.enchantmentKey))) {
            val nms = BuiltInRegistries.ENCHANTMENT[CraftNamespacedKey.toMinecraft(enchant.enchantmentKey)]

            if (nms != null) {
                return EcoEnchantsCraftEnchantment(enchant, nms)
            } else {
                throw IllegalStateException("Enchantment ${enchant.id} wasn't registered")
            }
        }

        Registry.register(BuiltInRegistries.ENCHANTMENT, enchant.id, VanillaEcoEnchantsEnchantment(enchant))

        return register(enchant)
    }

    override fun unregister(enchant: EcoEnchant) {
        /*

        You can't unregister from a minecraft registry, so we simply leave the stale reference there.
        This shouldn't cause many issues in production as the bukkit registry is replaced on each reload.

         */
    }
}

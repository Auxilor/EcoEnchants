package com.willfp.ecoenchants.proxy.v1_21

import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.impl.EcoEnchantBase
import com.willfp.ecoenchants.enchant.registration.modern.ModernEnchantmentRegistererProxy
import com.willfp.ecoenchants.proxy.v1_21.registration.EcoEnchantsCraftEnchantment
import com.willfp.ecoenchants.proxy.v1_21.registration.ModifiedVanillaCraftEnchantment
import com.willfp.ecoenchants.proxy.v1_21.registration.vanillaEcoEnchantsEnchantment
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantments
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.CraftRegistry
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.util.CraftNamespacedKey
import org.bukkit.enchantments.Enchantment
import java.util.IdentityHashMap
import java.util.function.BiFunction

val enchantmentRegistry =
    (Bukkit.getServer() as CraftServer).server.registryAccess().registryOrThrow(Registries.ENCHANTMENT)

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
        .filter { it.type == ResourceKey::class.java }
        .map { it.get(null) as ResourceKey<net.minecraft.world.item.enchantment.Enchantment> }
        .map { it.location() }
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
        @Suppress("DEPRECATION")
        minecraftToBukkit.set(org.bukkit.Registry.ENCHANTMENT, newRegistryMTB)

        // Unfreeze NMS registry
        frozenField.set(enchantmentRegistry, false)
        unregisteredIntrusiveHoldersField.set(
            enchantmentRegistry,
            IdentityHashMap<net.minecraft.world.item.enchantment.Enchantment,
                    Holder.Reference<net.minecraft.world.item.enchantment.Enchantment>>()
        )
    }

    override fun register(enchant: EcoEnchantBase): Enchantment {
        if (enchantmentRegistry.containsKey(CraftNamespacedKey.toMinecraft(enchant.enchantmentKey))) {
            val nms = enchantmentRegistry[CraftNamespacedKey.toMinecraft(enchant.enchantmentKey)]

            if (nms != null) {
                return EcoEnchantsCraftEnchantment(enchant, nms)
            } else {
                throw IllegalStateException("Enchantment ${enchant.id} wasn't registered")
            }
        }

        Registry.register(
            enchantmentRegistry,
            ResourceLocation.withDefaultNamespace(enchant.id),
            vanillaEcoEnchantsEnchantment(enchant)
        )

        return register(enchant)
    }

    override fun unregister(enchant: EcoEnchant) {
        /*

        You can't unregister from a minecraft registry, so we simply leave the stale reference there.
        This shouldn't cause many issues in production as the bukkit registry is replaced on each reload.

         */
    }
}

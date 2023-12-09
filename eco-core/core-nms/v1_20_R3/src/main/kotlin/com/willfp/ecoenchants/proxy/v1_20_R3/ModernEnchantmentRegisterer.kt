package com.willfp.ecoenchants.proxy.v1_20_R3

import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.registration.modern.ModernEnchantmentRegistererProxy
import com.willfp.ecoenchants.proxy.v1_20_R3.registration.EcoEnchantsCraftEnchantment
import com.willfp.ecoenchants.proxy.v1_20_R3.registration.ModifiedVanillaCraftEnchantment
import com.willfp.ecoenchants.proxy.v1_20_R3.registration.VanillaEcoEnchantsEnchantment
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.enchantment.Enchantments
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.CraftRegistry
import org.bukkit.craftbukkit.v1_20_R3.CraftServer
import org.bukkit.craftbukkit.v1_20_R3.util.CraftNamespacedKey
import org.bukkit.enchantments.Enchantment
import java.util.IdentityHashMap

class ModernEnchantmentRegisterer : ModernEnchantmentRegistererProxy {
    private val frozenField = MappedRegistry::class.java
        .declaredFields
        .filter { it.type.isPrimitive }[0]
        .apply { isAccessible = true }

    private val unregisteredIntrusiveHoldersField = MappedRegistry::class.java
        .declaredFields
        .last { it.type == Map::class.java }
        .apply { isAccessible = true }

    @Suppress("UNCHECKED_CAST")
    private val registries = CraftServer::class.java
        .getDeclaredField("registries")
        .apply { isAccessible = true }
        .get(Bukkit.getServer())
            as HashMap<Class<*>, org.bukkit.Registry<*>>

    private val vanillaEnchantments = Enchantments::class.java
        .declaredFields
        .filter { it.type == net.minecraft.world.item.enchantment.Enchantment::class.java }
        .map { it.get(null) as net.minecraft.world.item.enchantment.Enchantment }
        .mapNotNull { BuiltInRegistries.ENCHANTMENT.getKey(it) }
        .map { CraftNamespacedKey.fromMinecraft(it) }
        .toSet()

    override fun replaceRegistry() {
        val server = Bukkit.getServer() as CraftServer

        @Suppress("UNCHECKED_CAST")
        registries[Enchantment::class.java] = CraftRegistry(
            Enchantment::class.java as Class<in Enchantment?>,
            server.handle.server.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
        ) { key, registry ->
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
    }

    override fun register(enchant: EcoEnchant): Enchantment {
        if (BuiltInRegistries.ENCHANTMENT.containsKey(CraftNamespacedKey.toMinecraft(enchant.enchantmentKey))) {
            return org.bukkit.Registry.ENCHANTMENT.get(enchant.enchantmentKey)!!
        }

        // Unfreeze registry
        unfreeze(BuiltInRegistries.ENCHANTMENT)

        val nms = VanillaEcoEnchantsEnchantment(enchant.id)

        Registry.register(BuiltInRegistries.ENCHANTMENT, enchant.id, nms)

        return EcoEnchantsCraftEnchantment(enchant, nms)
    }

    private fun <T> unfreeze(registry: Registry<T>) {
        frozenField.set(registry, false)
        unregisteredIntrusiveHoldersField.set(registry, IdentityHashMap<T, Holder.Reference<T>>())
    }

    override fun unregister(enchant: EcoEnchant) {
        /*

        You can't unregister from a minecraft registry, so we simply leave the stale reference there.
        This shouldn't cause many issues in production because unregistered enchantments can't be accessed.

         */
    }
}

package com.willfp.ecoenchants.proxy.v1_21_4

import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.impl.EcoEnchantBase
import com.willfp.ecoenchants.enchant.registration.modern.ModernEnchantmentRegistererProxy
import com.willfp.ecoenchants.proxy.v1_21_4.registration.EcoEnchantsCraftEnchantment
import com.willfp.ecoenchants.proxy.v1_21_4.registration.ModifiedVanillaCraftEnchantment
import com.willfp.ecoenchants.proxy.v1_21_4.registration.vanillaEcoEnchantsEnchantment
import io.papermc.paper.registry.entry.RegistryTypeMapper
import io.papermc.paper.registry.legacy.DelayedRegistry
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.CraftRegistry
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.util.CraftNamespacedKey
import org.bukkit.enchantments.Enchantment
import java.lang.reflect.Modifier
import java.util.IdentityHashMap
import java.util.function.BiFunction
import javax.annotation.Nullable

private val enchantmentRegistry =
    (Bukkit.getServer() as CraftServer).server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)

@Suppress("DEPRECATION")
private val bukkitRegistry: org.bukkit.Registry<Enchantment>
    get() =
        (org.bukkit.Registry.ENCHANTMENT as DelayedRegistry<Enchantment, *>).delegate()

class ModernEnchantmentRegisterer : ModernEnchantmentRegistererProxy {
    private val frozenField = MappedRegistry::class.java
        .declaredFields
        .filter { it.type.isPrimitive }[0]
        .apply { isAccessible = true }

    private val allTags = MappedRegistry::class.java
        .declaredFields
        .filter { it.type.name.contains("TagSet") }[0]
        .apply { isAccessible = true }

    private val unregisteredIntrusiveHoldersField = MappedRegistry::class.java
        .declaredFields
        .filter { it.type == Map::class.java }
        .filter { it.isAnnotationPresent(Nullable::class.java) }[0]
        .apply { isAccessible = true }

    // 1.21.4+ only has minecraftToBukkit in CraftRegistry, removing the duplicate in WritableCraftRegistry
    private val minecraftToBukkit = CraftRegistry::class.java
        .getDeclaredField("minecraftToBukkit")
        .apply { isAccessible = true }

    private val cache = CraftRegistry::class.java
        .getDeclaredField("cache")
        .apply { isAccessible = true }

    override fun replaceRegistry() {
        val newRegistryMTB =
            BiFunction<NamespacedKey, net.minecraft.world.item.enchantment.Enchantment, Enchantment?> { key, registry ->
                val eco = EcoEnchants.getByID(key.key)
                val isRegistered = enchantmentRegistry.containsKey(CraftNamespacedKey.toMinecraft(key))

                if (eco != null) {
                    eco as Enchantment
                } else if (isRegistered) {
                    val holder = enchantmentRegistry.get(CraftNamespacedKey.toMinecraft(key)).get()

                    ModifiedVanillaCraftEnchantment(key, registry, holder)
                } else {
                    null
                }
            }

        // Update bukkit registry
        // The nasty casting hack is because of some weird nullability changes, if I set the BiFunction to have a
        // non-nullable bukkit enchantment type then it refuses to build, some sort of K2 compiler change.
        @Suppress("UNCHECKED_CAST")
        minecraftToBukkit.set(
            bukkitRegistry,
            RegistryTypeMapper(newRegistryMTB as BiFunction<NamespacedKey, net.minecraft.world.item.enchantment.Enchantment, Enchantment>)
        )

        // Clear the enchantment cache
        cache.set(bukkitRegistry, mutableMapOf<NamespacedKey, Enchantment>())

        // Unfreeze NMS registry
        frozenField.set(enchantmentRegistry, false)
        unregisteredIntrusiveHoldersField.set(
            enchantmentRegistry,
            IdentityHashMap<net.minecraft.world.item.enchantment.Enchantment,
                    Holder.Reference<net.minecraft.world.item.enchantment.Enchantment>>()
        )

        /*
        Creating an unbound tag set requires using reflection because the inner class is
        package-private, so we just find the method manually.
         */

        val unboundTagSet = MappedRegistry::class.java
            .declaredClasses[0]
            .declaredMethods
            .filter { Modifier.isStatic(it.modifiers) }
            .filter { it.parameterCount == 0 }[0]
            .apply { isAccessible = true }
            .invoke(null)

        allTags.set(enchantmentRegistry, unboundTagSet)
    }

    override fun register(enchant: EcoEnchantBase): Enchantment {
        // Clear the enchantment cache
        cache.set(bukkitRegistry, mutableMapOf<NamespacedKey, Enchantment>())

        if (enchantmentRegistry.containsKey(CraftNamespacedKey.toMinecraft(enchant.enchantmentKey))) {
            val nms = enchantmentRegistry[CraftNamespacedKey.toMinecraft(enchant.enchantmentKey)]

            if (nms.isPresent) {
                return EcoEnchantsCraftEnchantment(enchant, nms.get())
            } else {
                throw IllegalStateException("Enchantment ${enchant.id} wasn't registered")
            }
        }

        val vanillaEnchantment = vanillaEcoEnchantsEnchantment(enchant)

        enchantmentRegistry.createIntrusiveHolder(vanillaEnchantment)

        Registry.register(
            enchantmentRegistry,
            ResourceLocation.withDefaultNamespace(enchant.id),
            vanillaEnchantment
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

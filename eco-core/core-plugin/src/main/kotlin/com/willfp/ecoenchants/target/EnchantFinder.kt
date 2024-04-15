package com.willfp.ecoenchants.target

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.libreforge.ProvidedHolder
import com.willfp.libreforge.slot.ItemHolderFinder
import com.willfp.libreforge.slot.SlotType
import com.willfp.libreforge.toDispatcher
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.UUID
import java.util.concurrent.TimeUnit

object EnchantFinder : ItemHolderFinder<EcoEnchantLevel>() {
    private val levelCache = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.SECONDS)
        .build<UUID, List<ProvidedLevel>>()

    override fun find(item: ItemStack): List<EcoEnchantLevel> {
        val enchantMap = item.fast().enchants
        val enchants = mutableListOf<EcoEnchantLevel>()

        for ((enchant, level) in enchantMap) {
            if (enchant !is EcoEnchant) {
                continue
            }

            enchants += enchant.getLevel(level)
        }

        return enchants
    }

    override fun isValidInSlot(holder: EcoEnchantLevel, slot: SlotType): Boolean {
        return holder.enchant.targets.map { it.slot }.any { it.isOrContains(slot) }
    }

    internal fun LivingEntity.clearEnchantmentCache() = levelCache.invalidate(this.uniqueId)

    private val LivingEntity.cachedLevels: List<ProvidedLevel>
        get() = levelCache.get(this.uniqueId) {
            toHolderProvider().provide(this.toDispatcher())
                .mapNotNull {
                    val level = it.holder as? EcoEnchantLevel ?: return@mapNotNull null
                    val item = it.provider as? ItemStack ?: return@mapNotNull null

                    ProvidedLevel(level, item, it)
                }
        }

    fun LivingEntity.hasEnchantActive(enchant: EcoEnchant): Boolean {
        return this.cachedLevels
            .filter { it.level.enchant == enchant }
            .any { it.level.conditions.areMet(this.toDispatcher(), it.holder) }
    }

    fun LivingEntity.getItemsWithEnchantActive(enchant: EcoEnchant): Map<ItemStack, Int> {
        return this.cachedLevels
            .filter { it.level.enchant == enchant }
            .filter { it.level.conditions.areMet(this.toDispatcher(), it.holder) }
            .associate { it.item to it.level.level }
    }

    private data class ProvidedLevel(
        val level: EcoEnchantLevel,
        val item: ItemStack,
        val holder: ProvidedHolder
    )
}

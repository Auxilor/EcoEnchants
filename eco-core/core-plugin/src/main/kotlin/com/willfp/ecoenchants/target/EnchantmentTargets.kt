package com.willfp.ecoenchants.target

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.common.collect.ImmutableSet
import com.willfp.eco.core.items.HashedItem
import com.willfp.ecoenchants.EcoEnchantsPlugin
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

object EnchantmentTargets {
    private val BY_ID = mutableMapOf<String, EnchantmentTarget>()

    init {
        BY_ID["all"] = AllEnchantmentTarget
        update(EcoEnchantsPlugin.instance)
    }

    /**
     * Get EnchantTarget matching name.
     *
     * @param name The name to search for.
     * @return The matching EnchantTarget, or null if not found.
     */
    @JvmStatic
    fun getByID(name: String): EnchantmentTarget? {
        return BY_ID[name]
    }

    /**
     * Get target from item.
     *
     * @param item The item.
     * @return The target.
     */
    @JvmStatic
    fun getForItem(item: ItemStack): List<EnchantmentTarget> {
        return BY_ID.values
            .filter { !it.id.equals("all", ignoreCase = true) }
            .filter { it.matches(item) }
    }

    val ItemStack.isEnchantable: Boolean
        get() = enchantableCache.get(HashedItem.of(this)) {
            getForItem(this).isNotEmpty() || this.type == Material.BOOK || this.type == Material.ENCHANTED_BOOK
        }

    /**
     * Get all targets.
     *
     * @return A set of all targets.
     */
    @JvmStatic
    fun values(): Set<EnchantmentTarget> {
        return ImmutableSet.copyOf(BY_ID.values)
    }

    @JvmStatic
    fun update(plugin: EcoEnchantsPlugin) {
        for (target in values()) {
            if (target is AllEnchantmentTarget) {
                continue
            }
            removeTarget(target)
        }

        for (config in plugin.targetsYml.getSubsections("targets")) {
            ConfiguredEnchantmentTarget(config)
        }
    }

    /**
     * Remove [EnchantmentTarget] from EcoEnchants.
     *
     * @param target The [EnchantmentTarget] to remove.
     */
    @JvmStatic
    fun removeTarget(target: EnchantmentTarget) {
        BY_ID.remove(target.id)
    }

    /**
     * Add new [EnchantmentTarget] to EcoEnchants.
     *
     * Only for internal use, targets are automatically added in the
     * constructor.
     *
     * @param target The [EnchantmentTarget] to add.
     */
    internal fun addNewTarget(target: EnchantmentTarget) {
        BY_ID.remove(target.id)
        BY_ID[target.id] = target
    }
}

private val enchantableCache = Caffeine.newBuilder()
    .expireAfterAccess(5, TimeUnit.SECONDS)
    .build<HashedItem, Boolean>()

package com.willfp.ecoenchants.target

import com.willfp.eco.core.cache.EcoCache
import com.willfp.eco.core.items.HashedItem
import com.willfp.eco.core.registry.Registry
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.plugin
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.time.Duration

object EnchantmentTargets : Registry<EnchantmentTarget>() {
    init {
        register(AllEnchantmentTarget)
        update()
    }

    private fun getForItem(item: ItemStack): List<EnchantmentTarget> {
        return values()
            .filter { !it.id.equals("all", ignoreCase = true) }
            .filter { it.matches(item) }
    }

    val ItemStack.isEnchantable: Boolean
        get() = enchantableCache.get(HashedItem.of(this)) {
            getForItem(this).isNotEmpty() || this.type == Material.BOOK || this.type == Material.ENCHANTED_BOOK
        }

    val ItemStack.applicableEnchantments: List<EcoEnchant>
        get() = canEnchantCache.get(HashedItem.of(this)) {
            EcoEnchants.values().filter { it.canEnchantItem(this) }
        }

    @JvmStatic
    fun update() {
        val configs = plugin.targetsYml.getSubsections("targets")
        val configuredIds = configs.map { it.getString("id") }.toSet()

        for (target in values()) {
            if (target is AllEnchantmentTarget || target.id in configuredIds) {
                continue
            }
            remove(target)
        }

        for (config in configs) {
            val target = (this[config.getString("id")] as? ConfiguredEnchantmentTarget)
                ?.also { it.reload(config) }
                ?: ConfiguredEnchantmentTarget(config).also { register(it) }

            if (plugin.isLoaded) {
                for (invalid in target.invalidItems) {
                    plugin.logger.warning("Invalid item \"$invalid\" in target \"${target.id}\" in targets.yml, it will be ignored")
                }
            }
        }

        AllEnchantmentTarget.updateItems()
    }
}

private val enchantableCache = EcoCache.builder<HashedItem, Boolean>()
    .expireAfterAccess(Duration.ofSeconds(5))
    .build()

private val canEnchantCache = EcoCache.builder<HashedItem, List<EcoEnchant>>()
    .expireAfterAccess(Duration.ofSeconds(5))
    .build()

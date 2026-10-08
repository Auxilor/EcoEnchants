package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.enchant.VanillaEnchantmentOverrides
import com.willfp.ecoenchants.plugin
import org.bukkit.NamespacedKey

private fun getVanillaEnchantmentData(key: NamespacedKey): VanillaEnchantmentData? {
    val vanilla = plugin.vanillaEnchantsYml.getSubsectionOrNull(key.key) ?: return null

    return VanillaEnchantmentData(
        vanilla.getIntOrNull("max-level"),
        vanilla.getStringsOrNull("conflicts")?.map { NamespacedKey.minecraft(it) }
    )
}

object VanillaEnchantmentOverridesYml : VanillaEnchantmentOverrides {
    override fun getMaxLevel(key: NamespacedKey) = getVanillaEnchantmentData(key)?.maxLevel

    override fun getConflicts(key: NamespacedKey) = getVanillaEnchantmentData(key)?.conflicts
}

data class VanillaEnchantmentData(
    val maxLevel: Int?,
    val conflicts: Collection<NamespacedKey>?
)

private val enchantmentOptions = arrayOf(
    "max-level",
    "conflicts"
)

package com.willfp.ecoenchants.enchant

import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.plugin
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

val Enchantment.vanillaEnchantmentData: VanillaEnchantmentData?
    get() {
        val vanilla = plugin.vanillaEnchantsYml.getSubsectionOrNull(key.key) ?: return null

        return VanillaEnchantmentData(
            vanilla.getIntOrNull("max-level"),
            vanilla.getStringsOrNull("conflicts")?.map { NamespacedKey.minecraft(it) }
        )
    }

interface EcoCraftEnchantmentManagerProxy {
    fun registerNewCraftEnchantment(enchantment: Enchantment, data: VanillaEnchantmentData)
}

data class VanillaEnchantmentData(
    val maxLevel: Int?,
    val conflicts: Collection<NamespacedKey>?
)

private val enchantmentOptions = arrayOf(
    "max-level",
    "conflicts"
)

fun legacyRegisterVanillaEnchantmentData(plugin: EcoEnchantsPlugin) {
    for (vanilla in plugin.vanillaEnchantsYml.getKeys(false)) {
        if (enchantmentOptions.any { plugin.vanillaEnchantsYml.has("$vanilla.$it") }) {
            plugin.getProxy(EcoCraftEnchantmentManagerProxy::class.java).registerNewCraftEnchantment(
                Enchantment.getByKey(NamespacedKey.minecraft(vanilla))!!,
                VanillaEnchantmentData(
                    plugin.vanillaEnchantsYml.getIntOrNull("$vanilla.max-level"),
                    plugin.vanillaEnchantsYml.getStringsOrNull("$vanilla.conflicts")
                        ?.map { NamespacedKey.minecraft(it) }
                )
            )
        }
    }
}

package com.willfp.ecoenchants.enchant

import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.plugin
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

val Enchantment.vanillaEnchantmentData: VanillaEnchantmentData?
    get() {
        val configKey = if (key.key == "sweeping_edge") {
            "sweeping"
        } else {
            key.key
        }

        val vanilla = plugin.vanillaEnchantsYml.getSubsectionOrNull(configKey) ?: return null

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
        val key = if (vanilla == "sweeping_edge") {
            "sweeping"
        } else {
            vanilla
        }

        if (enchantmentOptions.any { plugin.vanillaEnchantsYml.has("$key.$it") }) {
            plugin.getProxy(EcoCraftEnchantmentManagerProxy::class.java).registerNewCraftEnchantment(
                Enchantment.getByKey(NamespacedKey.minecraft(vanilla))!!,
                VanillaEnchantmentData(
                    plugin.vanillaEnchantsYml.getIntOrNull("$key.max-level"),
                    plugin.vanillaEnchantsYml.getStringsOrNull("$key.conflicts")
                        ?.map { NamespacedKey.minecraft(it) }
                )
            )
        }
    }
}

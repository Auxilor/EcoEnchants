package com.willfp.ecoenchants.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getProvider
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object EffectRemoveThisEnchant : Effect<NoCompileData>("remove_this_enchant") {
    override val description = "Removes the enchantment running this effect from its item."

    override val categories = setOf("inventory")

    override val isPermanent = false

    override val additionalInfo = listOf(
        "Only works in an EcoEnchants enchantment.",
        "Use it with a trigger such as death to make an enchantment single use, for example with keep_inventory."
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val enchantLevel = data.holder.holder as? EcoEnchantLevel ?: return false
        val item = data.holder.getProvider<ItemStack>() ?: return false
        val meta = item.itemMeta ?: return false

        if (meta is EnchantmentStorageMeta) {
            meta.removeStoredEnchant(enchantLevel.enchant.enchantment)
        } else {
            meta.removeEnchant(enchantLevel.enchant.enchantment)
        }

        item.itemMeta = meta

        return true
    }
}

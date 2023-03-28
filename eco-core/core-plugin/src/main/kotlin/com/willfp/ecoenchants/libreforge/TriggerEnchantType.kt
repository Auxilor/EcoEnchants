package com.willfp.ecoenchants.libreforge

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.enchantment.EnchantItemEvent

class TriggerEnchantType(
    private val plugin: EcoPlugin,
    private val type: String
) : Trigger("enchant_$type") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.LOCATION,
        TriggerParameter.ITEM
    )

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun handleLevelling(event: EnchantItemEvent) {
        val player = event.enchanter

        plugin.scheduler.runLater({
            if (
                event.item.fast().getEnchants(true).keys
                    .filterIsInstance<EcoEnchant>()
                    .any { it.type.id.equals(type, ignoreCase = true) }
            ) {
                this.dispatch(
                    player,
                    TriggerData(
                        player = player,
                        location = player.location,
                        item = event.item,
                        value = event.expLevelCost.toDouble()
                    )
                )
            }
        }, 2)
    }
}

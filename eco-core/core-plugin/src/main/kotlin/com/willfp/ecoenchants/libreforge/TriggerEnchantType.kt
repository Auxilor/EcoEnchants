package com.willfp.ecoenchants.libreforge

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.enchantment.EnchantItemEvent

class TriggerEnchantType(
    private val plugin: EcoPlugin,
    private val type: EnchantmentType
) : Trigger("enchant_${type.id}") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.LOCATION,
        TriggerParameter.ITEM
    )

    override var isEnabled = true

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun handleLevelling(event: EnchantItemEvent) {
        val player = event.enchanter

        plugin.scheduler.runLater({
            if (
                event.item.fast().getEnchants(true).keys
                    .map { it.wrap() }
                    .any { it.type == type }
            ) {
                this.dispatch(
                    player.toDispatcher(),
                    TriggerData(
                        player = player,
                        location = player.location,
                        item = event.item,
                        value = event.expLevelCost.toDouble(),
                        text = type.id
                    )
                )
            }
        }, 2)
    }
}

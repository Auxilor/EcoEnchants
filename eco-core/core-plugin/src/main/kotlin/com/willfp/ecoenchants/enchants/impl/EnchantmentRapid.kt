package com.willfp.ecoenchants.enchants.impl

import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.target.EnchantLookup.getEnchantLevel
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

class EnchantmentRapid(
    plugin: EcoEnchantsPlugin
) : EcoEnchant(
    "rapid",
    plugin,
    force = false
) {
    override fun onInit() {
        this.registerListener(RapidHandler(this))
    }

    private class RapidHandler(
        private val enchant: EcoEnchant
    ) : Listener {
        @EventHandler(
            priority = EventPriority.LOW,
            ignoreCancelled = true
        )
        fun handle(event: EntityShootBowEvent) {
            val player = event.entity as? Player ?: return

            val level = player.getEnchantLevel(enchant)

            val multiplier = 1 - enchant.config.getDouble("percent-faster-per-level") * level / 100

            if (event.force < multiplier) {
                return
            }

            val force = 1 / event.force
            event.projectile.velocity = event.projectile.velocity.multiply(force)
        }
    }
}

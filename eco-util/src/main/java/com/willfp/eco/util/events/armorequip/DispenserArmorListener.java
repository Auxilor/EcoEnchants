package com.willfp.eco.util.events.armorequip;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Arnah
 * @since Feb 08, 2019
 */
public class DispenserArmorListener implements Listener {
    @EventHandler
    public void dispenseArmorEvent(@NotNull final BlockDispenseArmorEvent event) {
        ArmorType type = ArmorType.matchType(event.getItem());
        if (type != null && event.getTargetEntity() instanceof Player) {
            Player p = (Player) event.getTargetEntity();
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DISPENSER, type, null, event.getItem());
            Bukkit.getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }
}

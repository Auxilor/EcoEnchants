package com.willfp.ecoenchants.util.internal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;

public class ArrowListeners implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Arrow))
            return;
        if (!(event.getEntity().getShooter() instanceof LivingEntity))
            return;

        Arrow arrow = (Arrow) event.getEntity();
        LivingEntity entity = (LivingEntity) arrow.getShooter();

        if (entity.getEquipment() == null)
            return;

        ItemStack item = entity.getEquipment().getItemInMainHand();

        if (item == null) return;
        if (item.getType().equals(Material.AIR)) return;
        if (!item.hasItemMeta()) return;
        if (item.getItemMeta() == null) return;

        Map<Enchantment, Integer> enchantments = item.getItemMeta().getEnchants();
        arrow.setMetadata("enchantments", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), enchantments));
    }
}

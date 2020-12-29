package com.willfp.eco.util.arrows;

import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ArrowDataListener extends PluginDependent implements Listener {
    /**
     * Listener to add metadata to arrows about the enchantments on the bow that shot them.
     *
     * @param plugin The {@link AbstractEcoPlugin} that registered the listener.
     */
    @ApiStatus.Internal
    public ArrowDataListener(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Listener for arrows being shot by entities.
     *
     * @param event The {@link ProjectileLaunchEvent} passed by spigot.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLaunch(final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof LivingEntity)) {
            return;
        }

        Arrow arrow = (Arrow) event.getEntity();
        LivingEntity entity = (LivingEntity) arrow.getShooter();

        if (entity.getEquipment() == null) {
            return;
        }

        ItemStack item = entity.getEquipment().getItemInMainHand();

        if (item.getType().equals(Material.AIR) || !item.hasItemMeta() || item.getItemMeta() == null) {
            return;
        }

        Map<Enchantment, Integer> enchantments = item.getItemMeta().getEnchants();
        arrow.setMetadata("enchantments", this.getPlugin().getMetadataValueFactory().create(enchantments));
    }
}

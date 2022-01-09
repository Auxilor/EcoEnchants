package com.willfp.ecoenchants.enchantments.custom;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.events.ArmorChangeEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.libreforge.LibReforgeUtils;
import com.willfp.libreforge.effects.ConfiguredEffect;
import com.willfp.libreforge.events.EffectActivateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class CustomEcoEnchantRequirementListeners extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Initialize new listeners and link them to a plugin.
     *
     * @param plugin The plugin to link to.
     */
    public CustomEcoEnchantRequirementListeners(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    public void onActivate(@NotNull final EffectActivateEvent event) {
        if (!(event.getHolder() instanceof CustomEcoEnchantLevel level)) {
            return;
        }

        EcoEnchant enchant = level.getParent();

        if (!(enchant.areRequirementsMet(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }
}

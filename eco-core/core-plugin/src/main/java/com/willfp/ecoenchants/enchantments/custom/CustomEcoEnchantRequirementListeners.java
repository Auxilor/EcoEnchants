package com.willfp.ecoenchants.enchantments.custom;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.libreforge.events.EffectActivateEvent;
import org.bukkit.event.Listener;
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

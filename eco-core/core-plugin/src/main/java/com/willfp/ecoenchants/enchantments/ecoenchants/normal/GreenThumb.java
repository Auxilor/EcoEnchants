package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class GreenThumb extends EcoEnchant {
    public GreenThumb() {
        super(
                "green_thumb", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onInteract(@NotNull final PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        if (!event.getClickedBlock().getType().equals(Material.DIRT)) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!AntigriefManager.canBreakBlock(player, event.getClickedBlock())) {
            return;
        }

        if (!AntigriefManager.canPlaceBlock(player, event.getClickedBlock())) {
            return;
        }

        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "damage")) {
            DurabilityUtils.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());
        }

        event.getClickedBlock().setType(Material.GRASS_BLOCK);
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
public class GreenThumb extends EcoEnchant {
    public GreenThumb() {
        super(
                new EcoEnchantBuilder("green_thumb", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;

        if (event.getClickedBlock() == null)
            return;

        if (!event.getClickedBlock().getType().equals(Material.DIRT))
            return;

        if (!EnchantChecks.mainhand(player, this)) return;

        if(!AntigriefManager.canBreakBlock(player, event.getClickedBlock())) return;

        if(this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "damage"))
            ItemDurability.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());

        event.getClickedBlock().setType(Material.GRASS);
    }
}

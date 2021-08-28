package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.DurabilityUtils;
import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Farmhand extends EcoEnchant {
    public Farmhand() {
        super(
                "farmhand", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onTill(@NotNull final PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!(event.getClickedBlock().getType().equals(Material.DIRT) || event.getClickedBlock().getType().equals(Material.GRASS_BLOCK))) {
            return;
        }

        ItemStack item = event.getItem();

        if (!EnchantChecks.item(item, this)) {
            return;
        }
        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!item.getType().toString().endsWith("_HOE")) {
            return;
        }

        if (!AntigriefManager.canBreakBlock(player, event.getClickedBlock())) {
            return;
        }

        event.getClickedBlock().setType(Material.FARMLAND);
        int initial = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-radius");
        int levelrad = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-radius");
        int radius = initial + (EnchantChecks.getItemLevel(item, this) - 1) * levelrad;
        Vector[] vecs;

        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "use-cube")) {
            vecs = VectorUtils.getCube(radius);
        } else {
            vecs = VectorUtils.getSquare(radius);
        }

        if (!this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-block-damage")) {

            DurabilityUtils.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());
        }

        for (Vector vec : vecs) {
            Location loc = event.getClickedBlock().getLocation().add(vec);
            Block block = event.getClickedBlock().getWorld().getBlockAt(loc);

            if (!AntigriefManager.canBreakBlock(player, block)) {
                continue;
            }

            if (!(block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS_BLOCK))) {
                continue;
            }

            if (!block.getWorld().getBlockAt(loc.add(0, 1, 0)).getType().equals(Material.AIR)) {
                continue;
            }

            block.setType(Material.FARMLAND);
            if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-block-damage")) {

                DurabilityUtils.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());
            }
        }
    }
}

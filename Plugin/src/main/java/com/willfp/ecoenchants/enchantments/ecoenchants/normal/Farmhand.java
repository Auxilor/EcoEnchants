package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class Farmhand extends EcoEnchant {
    public Farmhand() {
        super(
                new EcoEnchantBuilder("farmhand", EnchantmentType.NORMAL, Target.Applicable.HOE, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onTill(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (event.getClickedBlock() == null)
            return;

        if (!(event.getClickedBlock().getType().equals(Material.DIRT) || event.getClickedBlock().getType().equals(Material.GRASS_BLOCK)))
            return;

        ItemStack item = event.getItem();

        if (!HasEnchant.item(item, this)) return;

        if (!(Target.Applicable.HOE.getMaterials().contains(item.getType())))
            return;

        if(!AntiGrief.canBreakBlock(player, event.getClickedBlock())) return;

        event.getClickedBlock().setType(Material.FARMLAND);
        int initial = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-radius");
        int levelrad = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-radius");
        int radius = initial + (HasEnchant.getItemLevel(item, this) - 1) * levelrad;
        Vector[] vecs;

        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "use-cube")) {
            vecs = Cube.getCube(radius);
        } else {
            vecs = Square.getSquare(radius);
        }

        if (!this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-block-damage")) {

            ItemDurability.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());
        }

        for (Vector vec : vecs) {
            Location loc = event.getClickedBlock().getLocation().add(vec);
            Block block = event.getClickedBlock().getWorld().getBlockAt(loc);

            if(!AntiGrief.canBreakBlock(player, block)) continue;

            if (!(block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS_BLOCK)))
                continue;

            if (!block.getWorld().getBlockAt(loc.add(0, 1, 0)).getType().equals(Material.AIR))
                continue;

            block.setType(Material.FARMLAND);
            if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-block-damage")) {

                ItemDurability.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());
            }
        }
    }
}

package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Farmhand extends EcoEnchant {
    public Farmhand() {
        super("farmhand", EnchantmentType.NORMAL);
    }

    @EventHandler
    public void onTill(@NotNull final PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) ||
                event.getClickedBlock() == null ||
                !this.areRequirementsMet(player) ||
                !(event.getClickedBlock().getType().equals(Material.DIRT) ||
                        event.getClickedBlock().getType().equals(Material.GRASS_BLOCK))) return;

        ItemStack item = event.getItem();
        if (!EnchantChecks.item(item, this) ||
                this.getDisabledWorlds().contains(player.getWorld()) ||
                !item.getType().toString().endsWith("_HOE") ||
                !AntigriefManager.canBreakBlock(player, event.getClickedBlock())) return;

        event.getClickedBlock().setType(Material.FARMLAND);
        int initial = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-radius");
        int levelrad = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-radius");
        int radius = initial + (EnchantChecks.getItemLevel(item, this) - 1) * levelrad;
        Vector[] vecs = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "use-cube") ? VectorUtils.getCube(radius) : VectorUtils.getSquare(radius);

        if (!this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-block-damage")) {
            if (player.getInventory().getItemInMainHand().isSimilar(item)) {
                damageItem(player, player.getInventory().getItemInMainHand(), 1);
            } else if (player.getInventory().getItemInOffHand().isSimilar(item)) {
                damageItem(player, player.getInventory().getItemInOffHand(), 1);
            }
        }

        for (Vector vec : vecs) {
            Location loc = event.getClickedBlock().getLocation().add(vec);
            Block block = event.getClickedBlock().getWorld().getBlockAt(loc);

            if (!AntigriefManager.canBreakBlock(player, block) ||
                    !(block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS_BLOCK)) ||
                    !block.getWorld().getBlockAt(loc.add(0, 1, 0)).getType().equals(Material.AIR)) continue;

            block.setType(Material.FARMLAND);
            if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-block-damage")) {
                if (player.getInventory().getItemInMainHand().isSimilar(item)) {
                    damageItem(player, player.getInventory().getItemInMainHand(), 1);
                } else if (player.getInventory().getItemInOffHand().isSimilar(item)) {
                    damageItem(player, player.getInventory().getItemInOffHand(), 1);
                }
            }
        }
    }

    private void damageItem(Player player, ItemStack item, int damage) {
        if (item.getItemMeta() == null || item.getItemMeta().isUnbreakable() ||
                item.getType() == Material.CARVED_PUMPKIN || item.getType() == Material.PLAYER_HEAD ||
                !(item.getItemMeta() instanceof Damageable)) return;

        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(meta.getDamage() + damage);

        if (meta.getDamage() >= item.getType().getMaxDurability()) {
            item.setAmount(item.getAmount() - 1);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1, 1);
        }

        item.setItemMeta(meta);
    }
}

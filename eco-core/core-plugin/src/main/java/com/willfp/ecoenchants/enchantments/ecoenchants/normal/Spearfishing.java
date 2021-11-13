package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Spearfishing extends EcoEnchant {
    public Spearfishing() {
        super(
                "spearfishing", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @EventHandler
    public void onSpearfishingLand(@NotNull final ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.TRIDENT) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!(event.getEntity() instanceof Trident trident)) {
            return;
        }

        if (!trident.getWorld().getBlockAt(trident.getLocation().add(0, 0.2, 0)).getType().equals(Material.WATER)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        ItemStack item = trident.getItem();

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        int level = EnchantChecks.getItemLevel(item, this);

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        List<Material> potentialDrops = new ArrayList<>();
        this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "drops", false).forEach(material -> {
            potentialDrops.add(Material.getMaterial(material.toUpperCase()));
        });

        Collections.shuffle(potentialDrops, new Random(NumberUtils.randInt(0, 100000)));
        ItemStack drop = new ItemStack(potentialDrops.get(0), 1);

        if (EnchantChecks.item(item, EcoEnchants.TELEKINESIS)) {
            new DropQueue(player)
                    .addItem(drop)
                    .setLocation(trident.getLocation())
                    .forceTelekinesis()
                    .push();
        } else {
            new DropQueue(player)
                    .addItem(drop)
                    .setLocation(trident.getLocation())
                    .push();
        }
    }
}

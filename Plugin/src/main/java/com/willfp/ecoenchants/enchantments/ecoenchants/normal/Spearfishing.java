package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.nms.TridentStack;
import com.willfp.ecoenchants.queue.DropQueue;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class Spearfishing extends EcoEnchant {
    public Spearfishing() {
        super(
                new EcoEnchantBuilder("spearfishing", EnchantmentType.NORMAL, Target.Applicable.TRIDENT, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onSpearfishingLand(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.TRIDENT)
            return;

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        if (!(event.getEntity() instanceof Trident)) return;

        Trident trident = (Trident) event.getEntity();

        if(!trident.getWorld().getBlockAt(trident.getLocation().add(0, 0.2, 0)).getType().equals(Material.WATER))
            return;

        Player player = (Player) event.getEntity().getShooter();

        ItemStack item = TridentStack.getTridentStack(trident);

        if (!HasEnchant.item(item, this)) return;

        int level = HasEnchant.getItemLevel(item, this);

        double chance = level * (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level")/100);
        if(Rand.randFloat(0, 1) > chance) return;

        List<Material> potentialDrops = new ArrayList<>();
        this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "drops").forEach(material -> {
            potentialDrops.add(Material.getMaterial(material.toUpperCase()));
        });

        Collections.shuffle(potentialDrops, new Random(Rand.randInt(0, 100000)));
        ItemStack drop = new ItemStack(potentialDrops.get(0), 1);

        new DropQueue(player)
                .addItem(drop)
                .setItem(item)
                .setLocation(trident.getLocation())
                .push();
    }
}
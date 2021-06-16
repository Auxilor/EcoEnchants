package com.willfp.ecoenchants.endershot;

import com.willfp.eco.core.integrations.mcmmo.McmmoManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Endershot extends EcoEnchant {
    public Endershot() {
        super("endershot", EnchantmentType.NORMAL);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBowShoot(@NotNull final EntityShootBowEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }
        if (event.getProjectile().getType() != EntityType.ARROW) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!player.isSneaking()) {
            return;
        }

        event.setCancelled(true);

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }
        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!player.getInventory().contains(Material.ENDER_PEARL, 1) && !player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        boolean hasInfinity = EnchantChecks.mainhand(player, ARROW_INFINITE) && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "work-with-infinity");
        if (!hasInfinity) {
            ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 1);
            player.getInventory().remove(pearl);
        }

        EnderPearl pearl = player.launchProjectile(EnderPearl.class);
        pearl.setShooter(player);
        pearl.setVelocity(event.getProjectile().getVelocity());
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1f, 1f);
    }
}

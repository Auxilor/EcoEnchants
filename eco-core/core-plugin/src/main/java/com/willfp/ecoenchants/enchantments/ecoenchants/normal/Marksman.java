package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jetbrains.annotations.NotNull;

public class Marksman extends EcoEnchant {
    public Marksman() {
        super(
                "marksman", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onMarksmanShoot(@NotNull final ProjectileLaunchEvent event) {
        if (event.getEntityType() != EntityType.ARROW) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }
        Arrow a = (Arrow) event.getEntity();
        a.setGravity(false);

        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "remove-arrow-after-ticks");

        this.getPlugin().getScheduler().runLater(() -> {
            if (!a.isOnGround()) {
                a.remove();
            }
        }, ticks);
    }
}

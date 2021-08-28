package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jetbrains.annotations.NotNull;

public class Marksman extends EcoEnchant {
    public Marksman() {
        super(
                "marksman", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onMarksmanShoot(@NotNull final ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        Projectile a = event.getEntity();
        a.setGravity(false);

        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "remove-arrow-after-ticks");

        this.getPlugin().getScheduler().runLater(() -> {
            if (!a.isOnGround()) {
                a.remove();
            }
        }, ticks);
    }
}

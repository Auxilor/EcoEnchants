package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jetbrains.annotations.NotNull;

public class Succession extends EcoEnchant {
    public Succession() {
        super(
                "succession", EnchantmentType.NORMAL
        );
    }

    public void onBowShoot(@NotNull final LivingEntity shooter,
                           final int level,
                           @NotNull final ProjectileLaunchEvent event) {

        boolean fire = EnchantChecks.mainhand(shooter, Enchantment.ARROW_FIRE);
        int per = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "arrows-per-level");
        for (int i = 1; i <= level*per; i++) {
            this.getPlugin().getScheduler().runLater(() -> {
                Arrow arrow1 = shooter.launchProjectile(Arrow.class, event.getEntity().getVelocity());
                arrow1.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                if (fire) {
                    arrow1.setFireTicks(Integer.MAX_VALUE);
                }

                if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-arrow-damage") && shooter instanceof Player) {
                    DurabilityUtils.damageItem((Player) shooter, ((Player) shooter).getInventory().getItemInMainHand(), 1, ((Player) shooter).getInventory().getHeldItemSlot());
                }
            }, i * 2L);
        }
    }
}

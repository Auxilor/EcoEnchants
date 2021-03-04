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
import org.jetbrains.annotations.NotNull;

public class Succession extends EcoEnchant {
    public Succession() {
        super(
                "succession", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBowShoot(@NotNull final LivingEntity shooter,
                           @NotNull final Arrow arrow,
                           final int level,
                           @NotNull final EntityShootBowEvent event) {
        int amount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "extra-arrows-per-level") * level;

        boolean fire = EnchantChecks.mainhand(shooter, Enchantment.ARROW_FIRE);

        for (int i = 1; i <= amount; i++) {
            this.getPlugin().getScheduler().runLater(() -> {
                Arrow arrow1 = shooter.launchProjectile(Arrow.class, event.getProjectile().getVelocity());
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
